package org.elasticsearch.mapper.annotations;

public @interface CompletionContext {
    String name();

    String type() default "category";

    String path() default "";

    String[] defaultVal() default {};
}
