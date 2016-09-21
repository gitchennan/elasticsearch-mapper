package org.elasticsearch.mapper.annotations;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Document {
    String indexName();

    String type() default "basic";

    short shards() default 1;

    short replicas() default 3;

    String refreshInterval() default "1s";

    String indexStoreType() default "fs";
}
