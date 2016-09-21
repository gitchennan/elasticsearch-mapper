package org.elasticsearch.mapper.annotations;

public enum FieldType {
    String,
    Integer,
    Long,
    Date,
    Float,
    Double,
    Boolean,
    Object,
    Auto,
    Nested,
    Ip;

    private FieldType() {
    }
}
