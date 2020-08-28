package com.luohh.playrole.exception;

import lombok.Data;

import java.io.Serializable;

/**
 * aip服务异常
 */
@Data
public class ApiServiceException extends Exception implements Serializable {
    /**
     * 编码
     */
    private int code;

    /**
     * 构造方法
     *
     * @param message
     * @param code
     */
    public ApiServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    /**
     * 构造方法
     *
     * @param code
     * @param message
     */
    public ApiServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ApiServiceException() {
    }

    public ApiServiceException(ApiServiceExceptionModel apiServiceExceptionModel) {
        super(apiServiceExceptionModel.getMsg());
        this.code = apiServiceExceptionModel.getCode();
    }

}
