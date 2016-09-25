package org.elasticsearch.mapper.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface FielddataFrequencyFilter {
    boolean enable() default false;

    double min();

    double max();

    int minSegmentSize();
}
