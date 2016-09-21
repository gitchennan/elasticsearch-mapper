package org.elasticsearch.mapper.test;

import org.elasticsearch.mapper.entity.Book;
import org.elasticsearch.mapper.parser.MappingBuilder;
import org.junit.Test;

import java.io.IOException;

public class MappingTest {
    @Test
    public void testGenMapping() throws IOException {
        String mapping = MappingBuilder.buildMapping(Book.class, "book").string();
        System.out.println(mapping);
    }
}
