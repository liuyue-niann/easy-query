package com.nn.query.annocation;

import java.lang.annotation.*;

/**
 * @author niann
 * @date 2024/2/2 18:41
 * @description 映射关系:多对一
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToOne {
    String value() default "";
}
