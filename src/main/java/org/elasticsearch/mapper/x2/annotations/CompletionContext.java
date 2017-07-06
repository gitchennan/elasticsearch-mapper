package org.elasticsearch.mapper.x2.annotations;

public @interface CompletionContext {
    String name();

    CompletionContextType type() default CompletionContextType.category;

    String path() default "";

    String[] defaultVal() default {};

    String[] precision() default {};

    boolean neighbors() default true;
}
