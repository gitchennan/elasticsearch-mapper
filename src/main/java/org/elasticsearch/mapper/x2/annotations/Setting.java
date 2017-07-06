package org.elasticsearch.mapper.x2.annotations;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Setting {
    String settingPath() default "";
}
