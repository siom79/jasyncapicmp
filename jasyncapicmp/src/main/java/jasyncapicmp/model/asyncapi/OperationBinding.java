package jasyncapicmp.model.asyncapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import jasyncapicmp.model.Model;
import jasyncapicmp.model.Reference;
import jasyncapicmp.model.asyncapi.kafka.KafkaOperationBinding;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OperationBinding implements Reference, Model {
    private KafkaOperationBinding kafka;
    @JsonProperty("$ref")
    private String ref;
}
