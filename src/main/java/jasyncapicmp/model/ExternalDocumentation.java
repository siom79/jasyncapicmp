package jasyncapicmp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExternalDocumentation implements Model {
    private String description;
    private String url;
}
