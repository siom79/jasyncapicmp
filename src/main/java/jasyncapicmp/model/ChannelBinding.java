package jasyncapicmp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jasyncapicmp.model.kafka.KafkaChannelBinding;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChannelBinding implements Reference, Model {
    private KafkaChannelBinding kafka;
    @JsonProperty("$ref")
    private String ref;
}
