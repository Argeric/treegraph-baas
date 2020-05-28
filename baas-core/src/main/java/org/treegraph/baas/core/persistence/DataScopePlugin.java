package org.treegraph.baas.core.persistence;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;
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
import org.treegraph.baas.common.util.PermManager;
//import cn.udesk.ccps.app.manager.sys.SysManager;
//import cn.udesk.ccps.app.thread.SkipDataScopeThreadLocal;
import org.treegraph.baas.core.constant.CharacterConstant;
import org.treegraph.baas.core.util.ReflectionUtils;

import com.google.common.collect.Sets;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopePlugin implements Interceptor {

    private List<String> appIdScopeModels;
    private Map<String, List<String>> companyMap;
    private Map<String, String> replaceAliasMap;
    private static final Map<String, Boolean> ignoreCache = new ConcurrentHashMap<>();

    private boolean ignore(String sign) {
        Boolean result = ignoreCache.get(sign);
        if (result == null) {
            try {
                Class<?> mapperClass = Class.forName(StringUtils.substringBeforeLast(sign, "."));
                boolean type = mapperClass.isAnnotationPresent(GlobalLevel.class);
                result = type || Arrays.stream(mapperClass.getDeclaredMethods()).anyMatch(method -> method.getName().equals(StringUtils.substringAfterLast(sign, ".")) &&
                        method.isAnnotationPresent(GlobalLevel.class));
            } catch (ClassNotFoundException e) {
                result = false;
            }
            ignoreCache.put(sign, result);
        }
        return result;
    }

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
            String tableName = StringUtils.substringBefore(StringUtils.substringAfterLast(idSub, CharacterConstant.DOT), SqlCommandType.SELECT == sqlCommandType ? "QueryMapper" : "CommandMapper");
            String methodId = StringUtils.substringAfterLast(id, CharacterConstant.DOT);
//            if (Objects.isNull(SkipDataScopeThreadLocal.get()) && (Objects.isNull(companyMap) || !(companyMap.containsKey(tableName) && (companyMap.get(tableName).contains(methodId) || companyMap.get(tableName).contains("all"))))) {
//                String sql = MultiTenantUtils.addTenantIsolatingCondition(sqlCommandType, delegate.getBoundSql().getSql(), "app_id = ", appIdScopeModels.contains(tableName) ? String.valueOf(getAppId()) : getAppSid());
//                ReflectionUtils.setFieldValue(boundSql, "sql", sql);
//            }
        }
        return invocation.proceed();
    }

//    private Long getAppId() {
//        return SysManager.getAppId();
//    }
//
//    private String getAppSid() {
//        return SysManager.getAppSid();
//    }
    
    private String getConsIdCond() {
    	return PermManager.getConsIdsCond();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // NOP
    }
}
