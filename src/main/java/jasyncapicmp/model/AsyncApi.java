package jasyncapicmp.model;

import com.fasterxml.jackson.databind.JsonNode;
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
public class AsyncApi implements Model {
    private String asyncapi;
    private String id;
    private Info info;
    private Map<String, Server> servers = new HashMap<>();
    private String defaultContentType;
    private Map<String, Channel> channels = new HashMap<>();
    private Map<String, Operation> operations = new HashMap<>();
    private Components components;
    private List<Tag> tags = new ArrayList<>();
    private ExternalDocumentation externalDocs;
    private JsonNode jsonNode;
}
