package org.treegraph.baas.api.rpc.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Scan系统统一返回结果
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScanResult<T> {
    public static final String DEFAULT_SUCCESS_STATUS = "0";
    public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功";
    public static final String DEFAULT_FAIL_STATUS = "9999";
    public static final String DEFAULT_FAIL_MESSAGE = "请求失败";

    private String code;
    private String message;
    private T result;

    public static <T> ScanResult<T> success() {
        return ScanResult.success(null);
    }

    public static <T> ScanResult<T> success(T data) {
        ScanResult<T> result = new ScanResult<>();
        result.code = DEFAULT_SUCCESS_STATUS;
        result.result = data;
        return result;
    }

    public static <T> ScanResult<T> success(String message, T data) {
        ScanResult<T> result = new ScanResult<>();
        result.code = DEFAULT_SUCCESS_STATUS;
        result.message = message;
        result.result = data;
        return result;
    }

    public static <T> ScanResult<T> fail() {
        return ScanResult.fail(DEFAULT_FAIL_STATUS, DEFAULT_FAIL_MESSAGE);
    }

    public static <T> ScanResult<T> fail(String message) {
        return ScanResult.fail(DEFAULT_FAIL_STATUS, message);
    }

    public static <T> ScanResult<T> fail(String status, String message) {
        ScanResult<T> result = new ScanResult<>();
        result.code = status;
        result.message = message;
        return result;
    }
}
