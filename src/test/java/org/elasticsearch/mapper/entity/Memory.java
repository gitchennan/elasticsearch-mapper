package org.elasticsearch.mapper.entity;


import org.elasticsearch.mapper.annotations.enums.NumberType;
import org.elasticsearch.mapper.annotations.fieldtype.NumberField;

public class Memory extends Component {
    @NumberField(type = NumberType.Byte)
    private Byte memorySize;
}
