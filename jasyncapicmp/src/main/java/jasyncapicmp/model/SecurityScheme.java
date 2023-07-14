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
public class SecurityScheme implements Reference, Model {
    private String type;
    private String description;
    @ListId
    private String name;
    private String in;
    private String scheme;
    private String bearerFormat;
    private OAuthFlows flows;
    private String openIdConnectUrl;
    @Version(since = Version.Versions.V3_0_0)
    private List<String> scopes = new ArrayList<>();
    @JsonProperty("$ref")
    private String ref;
}
