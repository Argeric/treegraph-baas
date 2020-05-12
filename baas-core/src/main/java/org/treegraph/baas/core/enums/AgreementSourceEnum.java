package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 合同来源枚举
 * @author GUANDI
 *
 */
@Getter
@AllArgsConstructor
public enum AgreementSourceEnum {
	NEW_CORE_AGREEMENT(1,"新核心"),
	OLD_CORE_AGREEMENT(2, "老核心"),
	AUTOMOBILE_FINANCE(3, "车金融"),
    ;
	
	
    private int    code;
    private String message;
}
