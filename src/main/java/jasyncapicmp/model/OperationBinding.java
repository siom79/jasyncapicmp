package jasyncapicmp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jasyncapicmp.model.kafka.KafkaOperationBinding;
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
