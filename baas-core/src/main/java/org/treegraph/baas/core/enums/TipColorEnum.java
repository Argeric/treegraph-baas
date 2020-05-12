package org.treegraph.baas.core.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipColorEnum {
	
	BLUE(1,"标蓝"),
	RED(2,"标红"),
    ;
	
    private int code;
    private String desc;
    
    public static TipColorEnum getByCode(int code) {
        return Arrays.stream(TipColorEnum.values())
                .filter(s -> s.getCode() == code).findFirst().orElse(null);
    }
}
