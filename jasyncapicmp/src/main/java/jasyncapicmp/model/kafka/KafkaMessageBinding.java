package jasyncapicmp.model.kafka;

import jasyncapicmp.model.Schema;

public class KafkaMessageBinding {
    private Schema key;
    private String schemaIdLocation;
    private String schemaIdPayloadEncoding;
    private String schemaLookupStrategy;
    private String bindingVersion;
}
