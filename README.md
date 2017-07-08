#elasticsearch-mapper
```java
org.elasticsearch.mapper.test.MappingTest


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

public class Component {

    private String serialNo;

    private String madeIn;
}

@Document(_type = "computer")
public class Computer {
    private String parentField;
}


public class User {

    private boolean isRoot;

    private String username;

    private String password;
}

public class Cpu extends Component {
    private int coreNumber;
}


public class Memory extends Component {
    @NumberField(type = NumberType.Byte)
    private Byte memorySize;
}

{
  "computer" : {
    "properties" : {
      "parentField" : {
        "type" : "keyword"
      }
    }
  }
}


{
  "macBook" : {
    "_all" : {
      "enabled" : false
    },
    "_parent" : {
      "type" : "computer"
    },
    "properties" : {
      "deviceName" : {
        "type" : "keyword"
      },
      "manufacturer" : {
        "type" : "text"
      },
      "introduction" : {
        "type" : "keyword",
        "boost" : 2.0,
        "fields" : {
          "pinyin" : {
            "type" : "text",
            "analyzer" : "lc_pinyin"
          },
          "cn" : {
            "type" : "text",
            "analyzer" : "ik_smart"
          },
          "en" : {
            "type" : "text",
            "analyzer" : "english"
          },
          "cnTokenCount" : {
            "type" : "token_count",
            "analyzer" : "ik_smart"
          }
        }
      },
      "users" : {
        "type" : "nested",
        "properties" : {
          "isRoot" : {
            "type" : "boolean"
          },
          "username" : {
            "type" : "keyword"
          },
          "password" : {
            "type" : "keyword"
          }
        }
      },
      "cpu" : {
        "type" : "object",
        "properties" : {
          "coreNumber" : {
            "type" : "integer"
          },
          "serialNo" : {
            "type" : "keyword"
          },
          "madeIn" : {
            "type" : "keyword"
          }
        }
      },
      "memory" : {
        "type" : "object",
        "properties" : {
          "memorySize" : {
            "type" : "byte"
          },
          "serialNo" : {
            "type" : "keyword"
          },
          "madeIn" : {
            "type" : "keyword"
          }
        }
      },
      "serialNo" : {
        "type" : "keyword"
      },
      "madeIn" : {
        "type" : "keyword"
      }
    }
  }
}

```