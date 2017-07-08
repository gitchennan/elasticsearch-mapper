package org.elasticsearch.mapper.test;

import org.elasticsearch.mapper.entity.MacBook;
import org.elasticsearch.mapper.mapper.MappingBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class MappingTest {
    @Test
    public void testGenMapping() throws IOException {
        MappingBuilder mappingBuilder = new MappingBuilder();
        Map<String, String> mappings = mappingBuilder.buildMappingAsString(MacBook.class);

        for (String typeName : mappings.keySet()) {
            System.out.println(mappings.get(typeName));
        }
    }
}
