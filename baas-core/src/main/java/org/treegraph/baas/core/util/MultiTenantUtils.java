package org.treegraph.baas.core.util;

import static org.treegraph.baas.core.constant.CharacterConstant.BLANK;
import static org.treegraph.baas.core.constant.CharacterConstant.DOT;
import static org.treegraph.baas.core.constant.CharacterConstant.OPEN_BRACKET;
import static org.treegraph.baas.core.constant.CharacterConstant.UNDERSCORE;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlCommandType;
import org.treegraph.baas.core.enums.ErrorCodeEnum;
import org.treegraph.baas.core.exception.BusinessException;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class MultiTenantUtils {

    private MultiTenantUtils() {

    }

    private static final int DEFAULT_LIMIT_THRESHOLD = 200;
    private static final String FROM_STATEMENT = "FROM";
    private static final String WHERE_STATEMENT = "WHERE";
    private static final String AND_STATEMENT = "AND";
    private static final String LIMIT_STATEMENT = "LIMIT";
	private static final Set<String> STATEMENT_SET = Sets.newHashSet("from", "join", "left", "right", "on", "where",
			"and", "select", "(select", "group", "by", "having", "order", "union", "all", "insert", "update", "set",
			"delete", "asc", "desc", "limit");
    private static final Set<String> SUFFIX_STATEMENT_SET = ImmutableSet.of("GROUP", "ORDER", LIMIT_STATEMENT);
    private static final String ONE = "1";
    private static final String START_CONDITION = "1 = 1";
    private static final String START_CONDITION_WITHOUT_BLANK = "1=1";
    private static final String COMPANY_ID_CONDITION = "company_id = ";

    /**
     * 主表必须有别名，如果为子查询，最外层的语句不会拼查询条件，对没有WHERE条件的语句不会拼查询条件，批量更新，删除
     *
     * @param sqlCommandType 命令类型
     * @param rawSql         原始SQL
     * @param companyId      公司ID
     * @return 处理过SQL
     */
	public static String addTenantIsolatingCondition(SqlCommandType sqlCommandType, String rawSql, long companyId) {
		return addTenantIsolatingConditionAndLimit(sqlCommandType, rawSql, COMPANY_ID_CONDITION,
				String.valueOf(companyId), true, DEFAULT_LIMIT_THRESHOLD);
    }

    /**
     * 主表必须有别名，如果为子查询，最外层的语句不会拼查询条件，对没有WHERE条件的语句不会拼查询条件，批量更新，删除
     *
     * @param sqlCommandType     命令类型
     * @param rawSql             原始SQL
     * @param isolatingCondition LIKE "consortium_id in "
     * @param consIdCond         LIKE "('a','b','c')"
     * @return 处理过SQL
     */
	public static String addTenantIsolatingCondition(SqlCommandType sqlCommandType, String rawSql,
			String isolatingCondition, String consIdCond) {
		return addTenantIsolatingConditionAndLimit(sqlCommandType, rawSql, isolatingCondition, consIdCond, true,
				DEFAULT_LIMIT_THRESHOLD);
	}
    
    /**
     * 主表必须有别名，如果为子查询，最外层的语句不会拼查询条件，对没有WHERE条件的语句不会拼查询条件，批量更新，删除
     *
     * @param sqlCommandType 命令类型
     * @param rawSql         原始SQL
     * @param tenantId       公司ID
     * @param limited        是否增加limit
     * @param limitThreshold limit阈值
     * @return 处理过SQL
     */
	public static String addTenantIsolatingConditionAndLimit(SqlCommandType sqlCommandType, String rawSql,
			String isolatingCondition, String tenantId, boolean limited, int limitThreshold) {
        if (StringUtils.isBlank(isolatingCondition)) {
            isolatingCondition = COMPANY_ID_CONDITION;
        }
        switch (sqlCommandType) {
            case SELECT:
                return parseSelect(rawSql, isolatingCondition, tenantId, limited, limitThreshold);
            case UPDATE:
            case DELETE:
                StringTokenizer rawSqlStringTokenizer = new StringTokenizer(rawSql);
                List<String> segmentSqlList = Lists.newArrayListWithExpectedSize(200);
                boolean hasWhereStatement = false;
                while (rawSqlStringTokenizer.hasMoreElements()) {
                    String nextElement = upperCaseStatement(rawSqlStringTokenizer.nextElement().toString());
                    segmentSqlList.add(nextElement);
                    if (StringUtils.equals(WHERE_STATEMENT, nextElement)) {
                        hasWhereStatement = true;
                        segmentSqlList.add(StringUtils.join(isolatingCondition, tenantId, BLANK, AND_STATEMENT));
                    }
                }
                if (!hasWhereStatement) {
                    segmentSqlList.add(StringUtils.join(WHERE_STATEMENT, BLANK, isolatingCondition, tenantId));
                }
                return StringUtils.join(segmentSqlList, BLANK);
            case INSERT:
                //TODO:
                return rawSql;
            default:
                return rawSql;
        }
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
                if (LIMIT_STATEMENT.equals(nextElement)) {
                    hasLimit = true;
                }
                suffixFlag = true;
                suffixSqlList.add(nextElement);
                continue;
            }
            if (fromStartNum > 0) {
                if (nextElement.startsWith(OPEN_BRACKET)) {
                    fromStartNum = 0;
                } else if (++fromStartNum > 2) {
                    fromStartNum = 0;
                    tableNameStack.push(nextElement);
                }
            }
            if (whereFlag) {
                if (startConditionNum > 0) {
                    startConditionStr = StringUtils.join(startConditionStr, nextElement);
                    if (startConditionNum++ > 1) {
                        startConditionNum = 0;
                        if (!startConditionStr.equals(START_CONDITION_WITHOUT_BLANK)) {
                            segmentSqlList.add(startConditionStr);
                        }
                    }
                    continue;
                } else if (nextElement.equals(ONE)) {
                    startConditionNum++;
                    startConditionStr = nextElement;
                    continue;
                } else if (nextElement.equals(START_CONDITION_WITHOUT_BLANK)) {
                    continue;
                } else {
                    whereFlag = false;
                    succeed = true;
                    //TODO: company_id条件重复
//                            if (nextElement.contains(COMPANY_ID)) {
//                                tableNameStack.pop();
//                            } else {
                    segmentSqlList.add(StringUtils.join(START_CONDITION, BLANK, AND_STATEMENT));
                    segmentSqlList.add(StringUtils.join(tableNameStack.pop(), DOT, isolatingCondition, tenantId));
//                            }
                    if (!AND_STATEMENT.equals(nextElement)) {
                        segmentSqlList.add(AND_STATEMENT);
                    }
                    segmentSqlList.add(nextElement);
                }
            } else {
                segmentSqlList.add(nextElement);
            }
            if (StringUtils.equals(FROM_STATEMENT, nextElement)) {
                fromStartNum++;
            }
            if (StringUtils.equals(WHERE_STATEMENT, nextElement)) {
                whereFlag = true;
            }
        }
        if (!tableNameStack.isEmpty()) {
            succeed = true;
            if (!StringUtils.equals(WHERE_STATEMENT, segmentSqlList.get(segmentSqlList.size() - 1))) {
                segmentSqlList.add(WHERE_STATEMENT);
            }
            String tableName = tableNameStack.pop();
            if (!UNDERSCORE.equals(tableName)) {
				segmentSqlList.add(StringUtils.join(START_CONDITION, BLANK, AND_STATEMENT, BLANK, tableName, DOT,
						isolatingCondition, tenantId));
            } else {
				segmentSqlList.add(
						StringUtils.join(START_CONDITION, BLANK, AND_STATEMENT, BLANK, isolatingCondition, tenantId));
            }
        }
        if (!succeed) {
			throw new BusinessException(ErrorCodeEnum.BUSINESS_EXCEPTION.getCode(),
					"add multi tenant isolating failed, sql = " + rawSql);
        }
        if (CollectionUtils.isNotEmpty(suffixSqlList)) {
            segmentSqlList.addAll(suffixSqlList);
        }
        if (limited && !hasLimit) {
            segmentSqlList.add(StringUtils.join(LIMIT_STATEMENT, BLANK, limitThreshold));
        }
        return StringUtils.join(segmentSqlList, BLANK);
    }

    @SuppressWarnings("unused")
	private static String parseUpdateOrDelete(String rawSql, String isolatingCondition, String tenantId) {
        StringTokenizer rawSqlStringTokenizer = new StringTokenizer(rawSql);
        boolean succeed = false;
        List<String> segmentSqlList = Lists.newArrayListWithExpectedSize(200);
        boolean hasWhereStatement = false;
        while (rawSqlStringTokenizer.hasMoreElements()) {
            String nextElement = upperCaseStatement(rawSqlStringTokenizer.nextElement().toString());
            segmentSqlList.add(nextElement);
            if (StringUtils.equals(WHERE_STATEMENT, nextElement)) {
                hasWhereStatement = true;
                succeed = true;
                segmentSqlList.add(StringUtils.join(isolatingCondition, tenantId, BLANK, AND_STATEMENT));
            }
        }
        if (!hasWhereStatement) {
            succeed = true;
            segmentSqlList.add(StringUtils.join(WHERE_STATEMENT, BLANK, isolatingCondition, tenantId));
        }
        if (!succeed) {
			throw new BusinessException(ErrorCodeEnum.BUSINESS_EXCEPTION.getCode(),
					"add multi tenant isolating failed, sql = " + rawSql);
        }
        return StringUtils.join(segmentSqlList, BLANK);
    }

    private static String upperCaseStatement(String segment) {
        if (STATEMENT_SET.contains(segment)) {
            return StringUtils.upperCase(segment);
        } else {
            return segment;
        }
    }
}
