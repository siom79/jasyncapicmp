package jasyncapicmp.model.kafka;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KafkaChannelBinding {
    private String topic;
    private int partitions;
    private int replicas;
    private KafkaTopicConfiguration topicConfiguration;
    private String bindingVersion;
}
