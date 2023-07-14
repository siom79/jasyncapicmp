package jasyncapicmp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ServerVariable implements Reference, Model {
    @JsonProperty("enum")
    private List<String> enums = new ArrayList<>();
    @JsonProperty("default")
    private String defaultValue;
    private String description;
    private List<String> examples = new ArrayList<>();
    @JsonProperty("$ref")
    private String ref;
}
