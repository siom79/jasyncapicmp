package jasyncapicmp.model.openapi;

import jasyncapicmp.model.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Header implements Model {
	private String description;
	private Schema schema;
	private boolean required;
	private boolean deprecated;
}
