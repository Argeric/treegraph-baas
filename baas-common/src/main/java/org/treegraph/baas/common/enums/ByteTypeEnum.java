package org.treegraph.baas.common.enums;

public enum ByteTypeEnum{
    /**
     * 真
     */
    TRUE((byte)1, "true"),
    
    /**
     * 假
     */
    FALSE((byte)0, "false"), 
    ;
    
    private Byte value;
    private String name;
    
    private ByteTypeEnum(Byte value, String name){
        this.value = value;
        this.name = name;
    }
    
    /**
     * 是否为真
     * @param value
     * @return
     */
    public static boolean isTrue(Byte value){
        return isSomeType(value, ByteTypeEnum.TRUE);
    }
    
    /**
     * 是否为假
     * @param value
     * @return
     */
    public static boolean isFalse(Byte value){
        return isSomeType(value, ByteTypeEnum.FALSE);
    }
    
    private static boolean isSomeType(Byte value, ByteTypeEnum type){
        if(null == value){
            return false;
        }
        return type.value.equals(value);
    }
    
    public Byte getValue(){
        return value;
    }
    
    public String getName(){
        return name;
    }
}
