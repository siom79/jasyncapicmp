package jasyncapicmp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jasyncapicmp.model.kafka.KafkaServerBinding;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServerBinding implements Reference, Model {
    private KafkaServerBinding kafka;
    @JsonProperty("$ref")
    private String ref;
}
