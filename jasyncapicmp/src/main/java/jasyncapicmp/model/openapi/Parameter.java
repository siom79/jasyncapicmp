package jasyncapicmp.model.openapi;

import jasyncapicmp.model.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Parameter implements Model {
	private String name;
	private String location;
	private String in;
	private boolean required;
	private Schema schema;
	private String style;
	private String description;
}
