package org.elasticsearch.mapper.x2.test;

import org.elasticsearch.mapper.x2.entity.Book;
import org.elasticsearch.mapper.x2.parser.MappingBuilder;
import org.junit.Test;

import java.io.IOException;

public class MappingTest {
    @Test
    public void testGenMapping() throws IOException {
        String mapping = MappingBuilder.buildMapping(Book.class).string();
        System.out.println(mapping);
    }
}
