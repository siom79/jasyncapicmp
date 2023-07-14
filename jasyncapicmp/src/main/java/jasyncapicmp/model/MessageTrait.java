package jasyncapicmp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class MessageTrait implements Reference, Model {
    @ListId
    private String messageId;
    private Schema headers;
    private CorrelationId correlationId;
    private String schemaFormat;
    private String contentType;
    private String name;
    private String title;
    private String summary;
    private String description;
    private List<Tag> tags = new ArrayList<>();
    private ExternalDocumentation externalDocs;
    private Map<String, MessageBinding> bindings = new HashMap<>();
    private List<MessageExample> examples = new ArrayList<>();
    @JsonProperty("$ref")
    private String ref;
}
