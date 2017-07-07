package org.elasticsearch.mapper.x5.test;

import org.elasticsearch.mapper.x5.entity.Book;
import org.elasticsearch.mapper.x5.mapper.MappingBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

public class MappingTest {
    @Test
    public void testGenMapping() throws IOException {
        MappingBuilder mappingBuilder = new MappingBuilder();
        Map<String, String> mappings = mappingBuilder.buildMappingAsString(Book.class);

        mappings.forEach(new BiConsumer<String, String>() {
            public void accept(String s, String s2) {
                System.out.println(s2);
            }
        });
    }
}
