package com.luohh.playrole.exception;


/**
 * 通用api服务异常定义
 */
public class ApiServiceExceptionDefinition {

    public static final ApiServiceExceptionModel API_REGISTER_FAILED = new ApiServiceExceptionModel(-99, "api注册失败");

    public static final ApiServiceExceptionModel UNKNOWN_EXCEPTION = new ApiServiceExceptionModel(-100, "系统未知异常");

    public static final ApiServiceExceptionModel USER_NOT_LOGIN = new ApiServiceExceptionModel(-101, "用户尚未登录或登录失效");

    public static final ApiServiceExceptionModel PARAM_CHECK_FAILED = new ApiServiceExceptionModel(-102, "参数校验失败");

    public static final ApiServiceExceptionModel API_NOT_EXISTS = new ApiServiceExceptionModel(-103, "API不存在");


}
