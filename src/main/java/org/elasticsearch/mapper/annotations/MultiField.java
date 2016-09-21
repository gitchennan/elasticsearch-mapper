package org.elasticsearch.mapper.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface MultiField {
    Field mainField();

    NestedField[] otherFields() default {};
}
