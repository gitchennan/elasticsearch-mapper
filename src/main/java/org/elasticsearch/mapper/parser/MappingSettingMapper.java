package org.elasticsearch.mapper.parser;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.mapper.annotations.Document;

import java.io.IOException;

public class MappingSettingMapper {
    public static final String META_FIELD_TIMESTAMP = "_timestamp";
    public static final String META_FIELD_ALL = "_all";
    public static final String META_FIELD_TTL = "_ttl";

    public void buildMapping(XContentBuilder mapping, Class clazz) throws IOException {
        Document setting = (Document) clazz.getAnnotation(Document.class);
        if (setting._timestamp()) {
            mapping.startObject(META_FIELD_TIMESTAMP).field("enabled", true).endObject();
        }
        if (!setting._all()) {
            mapping.startObject(META_FIELD_ALL).field("enabled", false).endObject();
        }
        if (setting._ttl().enabled()) {
            mapping.startObject(META_FIELD_TTL);
            mapping.field("enabled", true);
            mapping.field("default", setting._ttl()._default());
            mapping.endObject();
        }
    }
}
