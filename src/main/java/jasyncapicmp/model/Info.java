package jasyncapicmp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Info implements Model {
    private String title;
    private String version;
    private String description;
    private String termsOfService;
    private Contact contact;
    private License license;
    private List<Tag> tags = new ArrayList<>();
    private ExternalDocumentation externalDocs;
}
