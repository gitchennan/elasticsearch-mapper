package org.elasticsearch.mapper.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Inherited
public @interface Field {
    FieldType type() default FieldType.String;

    /*boolean,date*/
    FieldIndex index() default FieldIndex.not_analyzed;

    /*date*/
    DateFormat format() default DateFormat.none;

    /*boolean,date*/
    String nullValue() default "";
    /*date*/
    String pattern() default "";

    /*binary,boolean,date*/
    boolean store() default false;

    /*binary,boolean,date*/
    boolean docValues() default true;

    String searchAnalyzer() default "";

    String analyzer() default "";

    String[] ignoreFields() default {};

    boolean includeInParent() default false;

    /*boolean,date*/
    float boost() default 1.0f;

    /*date*/
    boolean ignoreMalformed() default false;

    /*date*/
    boolean includeInAll() default false;

    /*date*/
    int precisionStep() default 16;

}
