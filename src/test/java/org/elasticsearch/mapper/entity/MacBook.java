package org.elasticsearch.mapper.entity;

import org.elasticsearch.mapper.annotations.Document;
import org.elasticsearch.mapper.annotations.enums.StringType;
import org.elasticsearch.mapper.annotations.fieldtype.MultiField;
import org.elasticsearch.mapper.annotations.fieldtype.MultiNestedField;
import org.elasticsearch.mapper.annotations.fieldtype.StringField;
import org.elasticsearch.mapper.annotations.fieldtype.TokenCountField;
import org.elasticsearch.mapper.annotations.meta.MetaField_All;
import org.elasticsearch.mapper.annotations.meta.MetaField_Parent;

import java.util.List;

@Document(_type = "macBook", _all = @MetaField_All(enabled = false), _parent = @MetaField_Parent(parentClass = Computer.class))
public class MacBook extends Component {

    // default: keyword
    private String deviceName;

    @StringField(type = StringType.Text)
    private String manufacturer;

    @MultiField(
            mainField = @StringField(type = StringType.Keyword, boost = 2.0f),
            fields = {
                    @MultiNestedField(name = "pinyin", field = @StringField(type = StringType.Text, analyzer = "lc_pinyin")),
                    @MultiNestedField(name = "cn", field = @StringField(type = StringType.Text, analyzer = "ik_smart")),
                    @MultiNestedField(name = "en", field = @StringField(type = StringType.Text, analyzer = "english")),
            },
            tokenFields = {
                    @TokenCountField(name = "cnTokenCount", analyzer = "ik_smart")
            }
    )
    private String introduction;

    // nested doc
    private List<User> users;

    // inner doc
    private Cpu cpu;

    //inner doc
    private Memory memory;
}
