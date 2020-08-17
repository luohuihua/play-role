package com.luohh.playrole.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求api注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestApi {
    /**
     * 组别
     * @return
     */
    String group();

    /**
     * 描述
     * @return
     */
    String describe();
}
