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
public class Channel implements Reference, Model {
    private String address;
    private Map<String, Message> messages = new HashMap<>();
    private String title;
    private String summary;
    private String description;
    private List<Reference> servers = new ArrayList<>();
    private Map<String, Parameter> parameters = new HashMap<>();
    private List<Tag> tags = new ArrayList<>();
    private ExternalDocumentation externalDocs;
    private ChannelBinding bindings;
    @JsonProperty("$ref")
    private String ref;
    @Version(until = Version.Versions.V2_6_0)
    private Operation subscribe;
    @Version(until = Version.Versions.V2_6_0)
    private Operation publish;
}
