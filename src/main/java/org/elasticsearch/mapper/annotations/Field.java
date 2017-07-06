package org.elasticsearch.mapper.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Inherited
public @interface Field {
    FieldType type();

    /*boolean,date*/
    FieldIndex index() default FieldIndex._default;

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

    String[] ignoreFields() default {};

    boolean includeInParent() default false;

    /*boolean,date*/
    float boost() default 1.0f;

    /*date*/
    boolean ignoreMalformed() default false;

    /*date*/
    boolean includeInAll() default true;

    /*date*/
    int precisionStep() default 0;

    /*string*/
    Fielddata fielddata() default @Fielddata;

    /*string*/
    int ignoreAbove() default 0;

    /*string*/
    int positionIncrementGap() default 100;

    /*string*/
    Similarity similarity() default Similarity._default;

    /*string*/
    TermVectorx termVector() default TermVectorx.no;

    /*string*/
    String analyzer() default "";

    /*string*/
    String searchAnalyzer() default "";

    /*string*/
    String searchQuoteAnalyzer() default "";

    boolean coerce() default true;
}
