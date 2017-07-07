package org.elasticsearch.mapper.x2.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface MultiField {
    Field mainField();

    MultiNestedField[] otherFields() default {};
}
