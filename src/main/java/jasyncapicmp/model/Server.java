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
public class Server implements Reference, Model {
    @Version(until = Version.Versions.V2_6_0)
    private String url;
    @Version(since = Version.Versions.V3_0_0)
    private String host;
    private String protocol;
    private String protocolVersion;
    private String pathname;
    private String description;
    private String title;
    private String summary;
    private Map<String, ServerVariable> variables = new HashMap<>();
    @Version(since = Version.Versions.V3_0_0)
    private SecurityScheme securityScheme;
    @Version(until = Version.Versions.V2_6_0)
    private SecurityRequirement security;
    private List<Tag> tags = new ArrayList<>();
    private ExternalDocumentation externalDocs;
    private ServerBinding bindings;
    @JsonProperty("$ref")
    private String ref;
}
