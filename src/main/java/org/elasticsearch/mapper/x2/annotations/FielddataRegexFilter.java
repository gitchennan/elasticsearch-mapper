package org.elasticsearch.mapper.x2.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface FielddataRegexFilter {

    boolean enable() default false;

    String regex();
}
