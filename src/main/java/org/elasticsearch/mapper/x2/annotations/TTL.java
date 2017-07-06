package org.elasticsearch.mapper.x2.annotations;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TTL {
    boolean enabled() default false;
    String _default() default "";
}
