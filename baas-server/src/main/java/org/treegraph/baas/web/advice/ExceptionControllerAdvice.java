package org.treegraph.baas.web.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用异常处理器
 */
@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

  @ResponseBody
  @ExceptionHandler(BusinessException.class)
  public BaasResult<Void> handleRepaymentException(BusinessException e) {

    log.error("[userId={}]handleRepaymentException {}", WebHelper.getUserId(), e.getMessage(), e);
    return BaasResult.fail(e.getCode(), e.getMessage());
  }

  @ResponseBody
  @ExceptionHandler(AlertException.class)
  public BaasResult<Void> handleAlertException(AlertException e) {

    log.error("[userId={}]handleAlertException {}", WebHelper.getUserId(), e.getMessage(), e);
    return BaasResult.fail(e.getCode(), e.getMessage());
  }

  @ResponseBody
  @ExceptionHandler(IllegalArgumentException.class)
  public BaasResult<Void> handleIllegalArgumentException(IllegalArgumentException e) {

    log.error("[userId={}]handleIllegalArgumentException {}", WebHelper.getUserId(), e.getMessage(),
        e);
    return BaasResult.fail(9999, e.getMessage());
  }

  @ResponseBody
  @ExceptionHandler(IllegalStateException.class)
  public BaasResult<Void> handleIllegalStateException(IllegalStateException e) {

    log.error("[userId={}]handleIllegalStateException {}", WebHelper.getUserId(), e.getMessage(),
        e);
    return BaasResult.fail(9999, e.getMessage());
  }

  @ResponseBody
  @ExceptionHandler(Exception.class)
  public BaasResult<Void> handleException(Exception e) {

    log.error("[userId={}]handleException {}", WebHelper.getUserId(), e.getMessage(), e);
    return BaasResult.fail("系统繁忙，请稍后再试");
  }
}