package com.luohh.playrole.annotation.param;

import java.lang.annotation.*;

/**
 *非空注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface RequestNotNull {
}
