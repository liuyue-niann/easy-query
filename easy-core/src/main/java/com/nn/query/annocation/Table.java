package com.nn.query.annocation;

import jdk.jfr.Registered;

import java.lang.annotation.*;

/**
 * @author niann
 * @date 2024/2/2 13:09
 * @description 表名
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    String value() default "";
}
