package com.query.nn.annocation;

import java.lang.annotation.*;

/**
 * @author niann
 * @date 2024/2/2 14:49
 * @description
 **/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {

    String value() default "";

    boolean required() default true;
}
