package jasyncapicmp.model.asyncapi.kafka;

import jasyncapicmp.model.asyncapi.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KafkaOperationBinding {
    private Schema groupId;
    private Schema clientId;
    private String bindingVersion;
}
