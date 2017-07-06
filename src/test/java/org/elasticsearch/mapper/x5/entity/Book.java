package org.elasticsearch.mapper.x5.entity;


import org.elasticsearch.mapper.x5.annotations.Document;
import org.elasticsearch.mapper.x5.annotations.meta.MetaField_All;
import org.elasticsearch.mapper.x5.annotations.meta.MetaField_Routing;

@Document(_type = "book", _routing = @MetaField_Routing(required = true), _all = @MetaField_All(enabled = false))
public class Book {

}
