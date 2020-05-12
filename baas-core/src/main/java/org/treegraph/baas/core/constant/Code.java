package org.treegraph.baas.core.constant;

/**
 * @description:
 * @author: Guo Lixiao
 * @date: 2018-1-10 11:11
 * @see:
 * @since:
 **/
public interface Code {

    int SUCCESS         = 0;

    int E_2200          = 2200; //命中黑名单(APP使用)

    /** 4xx 请求异常 **/
    /** 参数错误 **/
    int E_400           = 400;

    /** 无权访问 **/
    int E_401           = 401;
    /** 登陆异常,需要重新登录 **/
    int E_405           = 405;
    int E_409 = 409;//冲突

    /** 5xx 系统异常 **/
    int E_503           = 503;
    
    int E_500 = 500;//系统异常
    
    //2000-2999  用户相关
    int E_2100 = 2100;//身份证已经被认证
    int E_2101 = 2101;//身份证已经过期
    
    int E_2300 = 2300;//银行卡三要素认证失败
    int E_2301 = 2301;//银行卡cardbin查询

    int E_2400 = 2400;//人脸识别比对失败
    int E_2401 = 2401;//活体认证暂时不能操作
    
    //ThreadPool
    int E_9603 = 9603;//ThreadPool RejectedExecutionException
    
    
    int E_5100 = 5100;//请求账务  查询还款计划接口异常
    int E_5101 = 5101;//请求账务  查询还款记录接口异常
    int E_5102 = 5102;//请求账务  查询还款银行卡接口异常

    int E_10000 = 10000;//仅用于最低版本拦截,只在拦截器中使用
    
}
