package org.elasticsearch.mapper.annotations;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MappingSetting {
    boolean timestamp() default false;
}
