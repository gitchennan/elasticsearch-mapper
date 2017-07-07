package org.elasticsearch.mapper.x2.annotations;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Document {
    String type();

    boolean _timestamp() default false;

    boolean _all() default true;

    TTL _ttl() default @TTL(enabled = false, _default = "5m");
}
