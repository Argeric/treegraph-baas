package org.treegraph.baas.core.exception;

import org.treegraph.baas.core.enums.ErrorCodeEnum;

public class WrongArgRepaymentException extends BusinessException {

	private static final long serialVersionUID = -2253112549857589942L;

	public WrongArgRepaymentException() {
        super(ErrorCodeEnum.INVALID_PARAMS);
    }

    public WrongArgRepaymentException(String message) {
        super(ErrorCodeEnum.INVALID_PARAMS, message);
    }
}