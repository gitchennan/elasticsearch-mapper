package org.elasticsearch.mapper.x5.annotations.fieldtype;

public @interface CompletionContext {

    String name();

    String type() default "category";

    String path() default "";
}
