package org.elasticsearch.mapper.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface Fielddata {
    FielddataFormat format() default FielddataFormat.paged_bytes;

    FielddataLoading loading() default FielddataLoading.lazy;

    FielddataFrequencyFilter frequency() default @FielddataFrequencyFilter(min = 0d, max = 0d, minSegmentSize = 0);

    FielddataRegexFilter regex() default @FielddataRegexFilter(regex = "");
}
