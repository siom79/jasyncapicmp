package jasyncapicmp.model.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class KafkaTopicConfiguration {
    @JsonProperty("cleanup.policy")
    private List<String> cleanupPolicy;
    @JsonProperty("retention.ms")
    private int retentionMs;
    @JsonProperty("retention.bytes")
    private int retentionBytes;
    @JsonProperty("delete.retention.ms")
    private int deleteRetentionMs;
    @JsonProperty("max.message.bytes")
    private int maxMessageBytes;
}
