package jasyncapicmp.model.asyncapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import jasyncapicmp.model.Model;
import jasyncapicmp.model.Reference;
import jasyncapicmp.model.asyncapi.kafka.KafkaMessageBinding;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageBinding implements Reference, Model {
    KafkaMessageBinding kafka;
    @JsonProperty("$ref")
    private String ref;
}
