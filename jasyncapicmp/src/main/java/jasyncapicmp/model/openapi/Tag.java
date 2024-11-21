package jasyncapicmp.model.openapi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Tag {
	private String name;
	private String description;
	private ExternalDocumentation externalDocs;
}
