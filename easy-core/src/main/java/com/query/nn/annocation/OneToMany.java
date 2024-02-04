package com.query.nn.annocation;

import java.lang.annotation.*;

/**
 * @author niann
 * @date 2024/2/2 18:38
 * @description 映射关系:一对多
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToMany {
    String value() default "";
}
