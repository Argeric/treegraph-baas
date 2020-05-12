package org.treegraph.baas.core.constant;

/**
 * @description:
 * @author: Guo Lixiao
 * @date: 2018-1-8 16:34
 * @see:
 * @since:
 **/
public interface LoanCode extends Code {

    //短信[3200]
    int E_3200          = 3200;//短信发送失败
    int E_3201          = 3201;//短信发送发送受限
    int E_3202          = 3202;//内容为空，不发短信

    int E_3203          = 3203;//验证码不正确
    int E_3204          = 3204;//验证码已过期
    int E_3292          = 3292;//今天验证码发送次数达到上限
    int E_3294          = 3294;//24小时内验证码发送次数达到上限
    int E_3295          = 3295;//不支持的业务类型

    int E_3208          = 3208;//短信发送提示（剩余2次和1次给予提示）
    int E_3209          = 3209;//数据冲突，重复发送短信

    int E_4200          = 4200;//业务关闭
    
    
    int E_5100          = 5100;//暂时没有支付结果
    int E_5101          = 5101;//支付结果查询结束

}
