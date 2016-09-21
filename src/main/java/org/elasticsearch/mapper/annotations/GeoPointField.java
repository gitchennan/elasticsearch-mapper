package org.elasticsearch.mapper.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface GeoPointField {
}
