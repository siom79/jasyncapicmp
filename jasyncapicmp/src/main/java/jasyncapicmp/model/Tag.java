package jasyncapicmp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Tag implements Reference, Model {
    @ListId
    private String name;
    private String description;
    private ExternalDocumentation externalDocs;
    @JsonProperty("$ref")
    private String ref;
}
