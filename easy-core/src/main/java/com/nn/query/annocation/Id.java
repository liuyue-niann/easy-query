package com.nn.query.annocation;

import java.lang.annotation.*;

/**
 * @author niann
 * @date 2024/2/2 13:10
 * @description
 **/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
    String value() default "";

    /**
     * 自增 true
     *
     * @return
     */
    boolean auto() default true;
}
