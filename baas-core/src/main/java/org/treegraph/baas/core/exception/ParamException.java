package org.treegraph.baas.core.exception;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-11-08
 * 参数错误
 */
public class ParamException extends RuntimeException {

	private static final long serialVersionUID = -8676584011542546463L;
	
	String param;
    String msg;
    String detail;

    public ParamException(String param, String msg, String detail) {
        this.param = param;
        this.msg = msg;
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public String getParam() {
        return param;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String getMessage() {
        return msg + "[" + param + "] " + detail;
    }

}
