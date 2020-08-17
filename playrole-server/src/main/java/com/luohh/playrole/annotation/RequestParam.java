package com.luohh.playrole.annotation;

import java.lang.annotation.*;

/**
 * 请求参数注解
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    /**
     * 名称
     *
     * @return
     */
    String name();

    /**
     * 请求类型
     *
     * @return
     */
    RequestParamType type() default RequestParamType.COMMON;

    /**
     * 描述
     *
     * @return
     */
    String describe() default "";

    /**
     * 默认值
     *
     * @return
     */
    String valueDef() default "";
}
