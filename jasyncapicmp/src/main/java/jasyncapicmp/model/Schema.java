package jasyncapicmp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Schema implements Reference, Model {
    @ListId
    private String title;
    private String type;
    private String description;
    private List<String> required = new ArrayList<>();
    private Map<String, Schema> properties = new HashMap<>();
    private Object additionalProperties;
    private Map<String, Schema> patternProperties = new HashMap<>();
    private int minLength;
    private int maxLength;
    private String format;
    private int minimum;
    private int maximum;
    private int multipleOf;
    private int exclusiveMinimum;
    private int exclusiveMaximum;
    private String pattern;
    private SchemaPattern propertyNames;
    private int minProperties;
    private int maxProperties;
    private Object items;
    private int minItems;
    private int maxItems;
    private boolean uniqueItems;
    @JsonProperty("enum")
    private List<String> enumeration;
    @JsonProperty("const")
    private String constant;
    private boolean readOnly;
    private boolean writeOnly;
    private List<Schema> allOf;
    private List<Schema> anyOf;
    private List<Schema> oneOf;
    private Schema not;
    @JsonProperty("$ref")
    private String ref;
}
