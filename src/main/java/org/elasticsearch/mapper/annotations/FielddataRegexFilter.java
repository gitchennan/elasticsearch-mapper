package org.elasticsearch.mapper.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface FielddataRegexFilter {

    boolean enable() default false;

    String regex();
}
