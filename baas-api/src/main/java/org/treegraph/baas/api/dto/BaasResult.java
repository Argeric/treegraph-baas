package org.treegraph.baas.api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Baas系统统一返回结果
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaasResult<T> {

  public static final int DEFAULT_SUCCESS_STATUS = 0;
  public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功";
  public static final int DEFAULT_FAIL_STATUS = 9999;
  public static final String DEFAULT_FAIL_MESSAGE = "请求失败";

  private int status;
  private String message;
  private T data;

//    public boolean isSuccess() {
//        return status == DEFAULT_SUCCESS_STATUS;
//    }

  public static <T> BaasResult<T> success() {
    return BaasResult.success(null);
  }

  public static <T> BaasResult<T> success(T data) {
    BaasResult<T> result = new BaasResult<>();
    result.status = DEFAULT_SUCCESS_STATUS;
    result.data = data;
    return result;
  }

  public static <T> BaasResult<T> success(String message, T data) {
    BaasResult<T> result = new BaasResult<>();
    result.status = DEFAULT_SUCCESS_STATUS;
    result.message = message;
    result.data = data;
    return result;
  }

  public static <T> BaasResult<T> fail() {
    return BaasResult.fail(DEFAULT_FAIL_STATUS, DEFAULT_FAIL_MESSAGE);
  }

  public static <T> BaasResult<T> fail(String message) {
    return BaasResult.fail(DEFAULT_FAIL_STATUS, message);
  }

  public static <T> BaasResult<T> fail(int status, String message) {
    BaasResult<T> result = new BaasResult<>();
    result.status = status;
    result.message = message;
    return result;
  }
}
