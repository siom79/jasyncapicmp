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
public class Operation implements Reference, Model {
    @Version(until = Version.Versions.V2_6_0)
    private String operationId;
    private String action;
    private ReferenceObject channel;
    private String title;
    private String summary;
    private String description;
    @Version(until = Version.Versions.V2_6_0)
    private SecurityRequirement security;
    private List<Tag> tags = new ArrayList<>();
    private ExternalDocumentation externalDocs;
    private OperationBinding bindings;
    private List<OperationTrait> traits = new ArrayList<>();
    private List<ReferenceObject> messages = new ArrayList<>();
    @Version(until = Version.Versions.V2_6_0)
    private Message message;
    private OperationReply reply;
    @JsonProperty("$ref")
    private String ref;
}
