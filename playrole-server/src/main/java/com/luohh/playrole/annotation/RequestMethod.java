package com.luohh.playrole.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求api方法注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMethod {
    /**
     * 描述
     *
     * @return
     */
    String describe();

    /**
     * 返回类型
     *
     * @return
     */
    ResultType resultType() default ResultType.COMMONS;

    /**
     * 返回名
     *
     * @return
     */
    String resultName() default "";

    /**
     * 最大限度
     *
     * @return
     */
    int maxAge() default -1;

    /**
     * 是否检查登录,默认是
     *
     * @return
     */
    boolean checkLogin() default true;

//    /**
//     * 权限许可码
//     *
//     * @return
//     */
//    String permission() default "";
//
//    /**
//     * 父类权限许可名称
//     *
//     * @return
//     */
//    String permissionParentName() default "";
//
//    /**
//     * 权限需求名
//     *
//     * @return
//     */
//    String permissionName() default "";
}
