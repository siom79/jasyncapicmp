package jasyncapicmp.model.asyncapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import jasyncapicmp.model.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OperationReplyAddress implements Reference, Model {
    private String description;
    private String location;
    @JsonProperty("$ref")
    private String ref;
}
