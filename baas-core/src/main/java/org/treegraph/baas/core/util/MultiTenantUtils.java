package org.treegraph.baas.core.util;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlCommandType;
import org.treegraph.baas.core.exception.AlertException;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class MultiTenantUtils {
	private static final int DEFAULT_LIMIT_THRESHOLD = 200;
	private static final String FROM_STATEMENT = "FROM";
	private static final String WHERE_STATEMENT = "WHERE";
	private static final String AND_STATEMENT = "AND";
	private static final String LIMIT_STATEMENT = "LIMIT";
	private static final Set<String> STATEMENT_SET = Sets.newHashSet(
			"from", "join", "left", "right", "on", "where", "and", "select", "(select", "group", "by",
					"having", "order", "union", "all", "insert", "update", "set", "delete", "asc", "desc", "limit" );
	private static final Set<String> SUFFIX_STATEMENT_SET = (Set<String>) ImmutableSet.of("GROUP", "ORDER", "LIMIT");

	private static final String ONE = "1";

	private static final String START_CONDITION = "1 = 1";

	private static final String START_CONDITION_WITHOUT_BLANK = "1=1";

	private static final String COMPANY_ID_CONDITION = "company_id = ";

	public static String addTenantIsolatingCondition(SqlCommandType sqlCommandType, String rawSql, long companyId) {
		return addTenantIsolatingConditionAndLimit(sqlCommandType, rawSql, "company_id = ", String.valueOf(companyId),
				true, 200);
	}

	public static String addTenantIsolatingCondition(SqlCommandType sqlCommandType, String rawSql,
			String isolatingCondition, String tenantId) {
		return addTenantIsolatingConditionAndLimit(sqlCommandType, rawSql, isolatingCondition,
				StringUtils.join( "'", tenantId, "'" ), true, 200);
	}

	public static String addTenantIsolatingConditionAndLimit(SqlCommandType sqlCommandType, String rawSql,
			String isolatingCondition, String tenantId, boolean limited, int limitThreshold) {
		StringTokenizer rawSqlStringTokenizer;
		List<String> segmentSqlList;
		boolean hasWhereStatement;
		if (StringUtils.isBlank(isolatingCondition)) {
			isolatingCondition = "company_id = ";
		}
		switch (sqlCommandType) {
		case SELECT:
			return parseSelect(rawSql, isolatingCondition, tenantId, limited, limitThreshold);
		case UPDATE:
		case DELETE:
			rawSqlStringTokenizer = new StringTokenizer(rawSql);
			segmentSqlList = Lists.newArrayListWithExpectedSize(200);
			hasWhereStatement = false;
			while (rawSqlStringTokenizer.hasMoreElements()) {
				String nextElement = upperCaseStatement(rawSqlStringTokenizer.nextElement().toString());
				segmentSqlList.add(nextElement);
				if (StringUtils.equals("WHERE", nextElement)) {
					hasWhereStatement = true;
					segmentSqlList.add(StringUtils.join(isolatingCondition, tenantId, " ", "AND" ));
				}
			}
			if (!hasWhereStatement) {
				segmentSqlList.add(StringUtils.join("WHERE", " ", isolatingCondition, tenantId ));
			}
			return StringUtils.join(segmentSqlList, " ");

		case INSERT:
			return rawSql;
		}
		return rawSql;
	}

	private static String parseSelect(String rawSql, String isolatingCondition, String tenantId, boolean limited,
			int limitThreshold) {
		
		StringTokenizer rawSqlStringTokenizer = new StringTokenizer(rawSql);
		
		boolean succeed = false;
		
		List<String> segmentSqlList = Lists.newArrayListWithExpectedSize(200);
		
		boolean suffixFlag = false;
		
		List<String> suffixSqlList = Lists.newArrayListWithExpectedSize(20);
		
		Deque<String> tableNameStack = new ArrayDeque<>();
		
		int fromStartNum = 0;
		
		int startConditionNum = 0;
		
		String startConditionStr = null;
		
		boolean whereFlag = false;
		
		boolean hasLimit = false;
		
		while (rawSqlStringTokenizer.hasMoreElements()) {
			
			String nextElement = upperCaseStatement(rawSqlStringTokenizer.nextElement().toString());
			
			if (suffixFlag || SUFFIX_STATEMENT_SET.contains(nextElement)) {
				if ("LIMIT".equals(nextElement)) {
					hasLimit = true;
				}
				suffixFlag = true;
				suffixSqlList.add(nextElement);
				continue;
			}
			
			if (fromStartNum > 0) {
				if (nextElement.startsWith("(")) {
					fromStartNum = 0;
				} else if (++fromStartNum > 2) {
					fromStartNum = 0;
					tableNameStack.push(nextElement);
				}
			}
			
			if (whereFlag) {
				if (startConditionNum > 0) {
					startConditionStr = StringUtils.join((Object[]) new String[] { startConditionStr, nextElement });
					if (startConditionNum++ > 1) {
						startConditionNum = 0;
						if (!startConditionStr.equals("1=1"))
							segmentSqlList.add(startConditionStr);
					}
					continue;
				}
				if (nextElement.equals("1")) {
					startConditionNum++;
					startConditionStr = nextElement;
					continue;
				}
				if (nextElement.equals("1=1")) {
					continue;
				}
				whereFlag = false;
				succeed = true;

				segmentSqlList.add(StringUtils.join((Object[]) new String[] { "1 = 1", " ", "AND" }));
				segmentSqlList.add(StringUtils
						.join((Object[]) new String[] { tableNameStack.pop(), ".", isolatingCondition, tenantId }));

				if (!"AND".equals(nextElement)) {
					segmentSqlList.add("AND");
				}
				segmentSqlList.add(nextElement);
			} else {

				segmentSqlList.add(nextElement);
			}
			
			if (StringUtils.equals("FROM", nextElement)) {
				fromStartNum++;
			}
			
			if (StringUtils.equals("WHERE", nextElement)) {
				whereFlag = true;
			}
		}
		
		if (!tableNameStack.isEmpty()) {
			succeed = true;
			if (!StringUtils.equals("WHERE", segmentSqlList.get(segmentSqlList.size() - 1))) {
				segmentSqlList.add("WHERE");
			}
			String tableName = tableNameStack.pop();
			if (!"_".equals(tableName)) {
				segmentSqlList.add(StringUtils.join((Object[]) new String[] { "1 = 1", " ", "AND", " ", tableName, ".",
						isolatingCondition, tenantId }));
			} else {
				segmentSqlList.add(StringUtils
						.join((Object[]) new String[] { "1 = 1", " ", "AND", " ", isolatingCondition, tenantId }));
			}
		}
		if (!succeed) {
			throw new AlertException(9999, "add multi tenant isolating failed, sql = " + rawSql);
		}
		if (CollectionUtils.isNotEmpty(suffixSqlList)) {
			segmentSqlList.addAll(suffixSqlList);
		}
		if (limited && !hasLimit) {
			segmentSqlList.add(
					StringUtils.join((Object[]) new Serializable[] { "LIMIT", " ", Integer.valueOf(limitThreshold) }));
		}
		
		return StringUtils.join(segmentSqlList, " ");
	}

	private static String parseUpdateOrDelete(String rawSql, String isolatingCondition, String tenantId) {
		StringTokenizer rawSqlStringTokenizer = new StringTokenizer(rawSql);
		boolean succeed = false;
		List<String> segmentSqlList = Lists.newArrayListWithExpectedSize(200);
		boolean hasWhereStatement = false;
		while (rawSqlStringTokenizer.hasMoreElements()) {
			String nextElement = upperCaseStatement(rawSqlStringTokenizer.nextElement().toString());
			segmentSqlList.add(nextElement);
			if (StringUtils.equals("WHERE", nextElement)) {
				hasWhereStatement = true;
				succeed = true;
				segmentSqlList
						.add(StringUtils.join((Object[]) new String[] { isolatingCondition, tenantId, " ", "AND" }));
			}
		}
		if (!hasWhereStatement) {
			succeed = true;
			segmentSqlList
					.add(StringUtils.join((Object[]) new String[] { "WHERE", " ", isolatingCondition, tenantId }));
		}
		if (!succeed) {
			throw new AlertException(9999, "add multi tenant isolating failed, sql = " + rawSql);
		}
		return StringUtils.join(segmentSqlList, " ");
	}

	private static String upperCaseStatement(String segment) {
		if (STATEMENT_SET.contains(segment)) {
			return StringUtils.upperCase(segment);
		}
		return segment;
	}
	
	public static void main(String[] args) {
		String rawSql = "select p.permission_code from permission p LEFT JOIN role_permission rp on p.id = rp.permission_id LEFT JOIN user_role ur on rp.role_id = ur.role_id where ur.user_id = '72abc140-3279-4b58-a8f5-433c87387c82' ";
		rawSql = "update user_role set delete_flag = 0 where role_id in (select r.id from role r where r.role_name = '系统管理员')";
		//rawSql = "select r.consortium_id from role r where r.id in (select distinct ur.role_id from user_role ur where ur.user_id = '72abc140-3279-4b58-a8f5-433c87387c82')";
		String sql = MultiTenantUtils.addTenantIsolatingCondition(SqlCommandType.UPDATE, rawSql, "consortium_id = ", "333444");
		System.out.println(sql);
	}
}