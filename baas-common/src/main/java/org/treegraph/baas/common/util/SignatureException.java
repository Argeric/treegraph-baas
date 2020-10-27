package org.treegraph.baas.common.util;

public class SignatureException extends RuntimeException {
	private static final long serialVersionUID = 1L;

//	public SignatureException() {
//	}
//
//	public SignatureException(String message) {
//		super(message);
//	}
//
//	public SignatureException(String message, Throwable cause) {
//		super(message, cause);
//	}

	public SignatureException(Throwable cause) {
		super(cause);
	}

//	protected SignatureException(String message, Throwable cause, boolean enableSuppression,
//			boolean writableStackTrace) {
//		super(message, cause, enableSuppression, writableStackTrace);
//	}
}
