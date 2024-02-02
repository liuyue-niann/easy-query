package com.nn.annocation;

import java.lang.annotation.*;

/**
 * @author niann
 * @date 2024/2/2 18:41
 * @description
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Many {
    String value() default "";
}
