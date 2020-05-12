package org.treegraph.baas.core.enums;
public enum MsgRecordStatusEnum {
	FAIL(1,"fail"),
    SUCCESS(2,"success"),
    CANCEL(3,"cancel"),
    ;
    private int code;
    private String msg;

    MsgRecordStatusEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}