package org.elasticsearch.mapper.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Inherited
public @interface CompletionField {
    String searchAnalyzer() default "simple";

    String analyzer() default "simple";

    boolean payloads() default false;

    boolean preserveSeparators() default true;

    boolean preservePositionIncrements() default true;

    int maxInputLength() default 50;

    CompletionContext[] context() default {};
}
