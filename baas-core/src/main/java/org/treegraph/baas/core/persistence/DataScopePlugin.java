package org.treegraph.baas.core.persistence;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.treegraph.baas.common.util.PermHelper;
import org.treegraph.baas.common.util.SkipDataScopeThreadLocal;
import org.treegraph.baas.core.constant.CharacterConstant;
import org.treegraph.baas.core.util.MultiTenantUtils;
import org.treegraph.baas.core.util.ReflectionUtils;

import lombok.Data;

@Data
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopePlugin implements Interceptor {

    private Map<String, List<String>> globalMethod;
    
    private static final Map<String, Boolean> ignoreCache = new ConcurrentHashMap<>();
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
        StatementHandler delegate = (StatementHandler) ReflectionUtils.getFieldValue(handler, "delegate");
        ParameterHandler parameterHandler = delegate.getParameterHandler();
        MappedStatement mappedStatement = (MappedStatement) ReflectionUtils.getFieldValue(parameterHandler, "mappedStatement");
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        BoundSql boundSql = delegate.getBoundSql();
        String id = mappedStatement.getId();
        if (!ignore(id)) {
            String idSub = StringUtils.substringBeforeLast(id, CharacterConstant.DOT);
            String tableName = StringUtils.substringBefore(StringUtils.substringAfterLast(idSub, CharacterConstant.DOT), "PoMapper");
            String methodId = StringUtils.substringAfterLast(id, CharacterConstant.DOT);
			if (Objects.isNull(SkipDataScopeThreadLocal.get()) && (
					Objects.isNull(globalMethod) 
					|| !(globalMethod.containsKey(tableName)
					   && (globalMethod.get(tableName).contains(methodId) || globalMethod.get(tableName).contains("all")))
					)
				) {
				String sql = MultiTenantUtils.addTenantIsolatingCondition(sqlCommandType,
						delegate.getBoundSql().getSql(), "consortium_id in ", getConsIdCond());
                ReflectionUtils.setFieldValue(boundSql, "sql", sql);
            }
        }
        return invocation.proceed();
    }
    
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // NOP
    }

    private boolean ignore(String sign) {
    	if(PermHelper.getSysAdminFlag()) {
    		return true;
    	}
    	
        Boolean result = ignoreCache.get(sign);
        if (result == null) {
            try {
                Class<?> mapperClass = Class.forName(StringUtils.substringBeforeLast(sign, "."));
                boolean type = mapperClass.isAnnotationPresent(GlobalLevel.class);
				result = type || Arrays.stream(mapperClass.getDeclaredMethods()).anyMatch(method -> 
					method.getName().equals(StringUtils.substringAfterLast(sign, "."))
							&& method.isAnnotationPresent(GlobalLevel.class)
				);
            } catch (ClassNotFoundException e) {
                result = false;
            }
            ignoreCache.put(sign, result);
        }
        return result;
    }
    
    private String getConsIdCond() {
    	return PermHelper.getConsIdsCond();
    }
}
