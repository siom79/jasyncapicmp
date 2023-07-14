package jasyncapicmp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OperationReply implements Reference, Model {
    private OperationReplyAddress address;
    private List<String> messages;
    @JsonProperty("$ref")
    private String ref;
}
