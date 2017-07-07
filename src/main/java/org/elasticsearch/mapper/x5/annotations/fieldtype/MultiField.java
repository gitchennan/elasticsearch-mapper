package org.elasticsearch.mapper.x5.annotations.fieldtype;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface MultiField {
    /**
     * The main string field
     */
    StringField mainField();

    /**
     * Multi-fields allow the same string value to be indexed in multiple ways for different purposes,
     * such as one field for search and a multi-field for sorting and aggregations.
     */
    MultiNestedField[] fields() default {};

}
