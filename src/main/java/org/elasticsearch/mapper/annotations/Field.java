package org.elasticsearch.mapper.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Inherited
public @interface Field {
    FieldType type() default FieldType.String;

    FieldIndex index() default FieldIndex.analyzed;

    DateFormat format() default DateFormat.none;

    String defaultValue() default "";

    String pattern() default "";

    boolean store() default false;

    String searchAnalyzer() default "";

    String indexAnalyzer() default "";

    String[] ignoreFields() default {};

    boolean includeInParent() default false;
}
