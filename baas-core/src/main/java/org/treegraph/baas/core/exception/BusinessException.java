package org.treegraph.baas.core.exception;

import org.treegraph.baas.core.enums.ErrorCodeEnum;

import lombok.Getter;

/**
 * 系统的业务异常基类，所有特定的业务异常继承这个类
 */
@Getter
public class BusinessException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final int code;
  private final String message;


  public BusinessException(ErrorCodeEnum code) {
    this(code.getCode(), code.getMessage());
  }

  public BusinessException(ErrorCodeEnum code, String message) {
    this(code.getCode(), message);
  }

  public BusinessException(int code, String message) {
    super(message);
    this.code = code;
    this.message = message;
  }
}