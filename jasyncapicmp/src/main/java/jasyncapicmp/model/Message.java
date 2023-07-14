package jasyncapicmp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Message implements Reference, Model {
    @ListId
    private String messageId;
    private Schema headers;
    private Schema payload;
    private CorrelationId correlationId;
    private String schemaFormat;
    private String contentType;
    private String name;
    private String title;
    private String summary;
    private String description;
    private List<Tag> tags = new ArrayList<>();
    private ExternalDocumentation externalDocs;
    private MessageBinding bindings;
    private List<MessageExample> examples;
    private List<MessageTrait> traits;
    private List<Message> oneOf;
    @JsonProperty("$ref")
    private String ref;
}
