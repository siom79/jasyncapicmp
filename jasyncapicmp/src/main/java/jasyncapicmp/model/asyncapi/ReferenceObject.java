package jasyncapicmp.model.asyncapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import jasyncapicmp.model.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReferenceObject implements Reference, Model {
    @JsonProperty("$ref")
    private String ref;
}
