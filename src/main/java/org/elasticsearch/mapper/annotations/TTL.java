package org.elasticsearch.mapper.annotations;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TTL {
    boolean enabled() default false;
    String _default() default "";
}
