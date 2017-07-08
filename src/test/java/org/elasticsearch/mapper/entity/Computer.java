package org.elasticsearch.mapper.entity;

import org.elasticsearch.mapper.annotations.Document;

/**
 * I'm MacBook's father
 */
@Document(_type = "computer")
public class Computer {
    private String parentField;
}
