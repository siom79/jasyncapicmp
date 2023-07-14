package jasyncapicmp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jasyncapicmp.model.kafka.KafkaMessageBinding;
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
