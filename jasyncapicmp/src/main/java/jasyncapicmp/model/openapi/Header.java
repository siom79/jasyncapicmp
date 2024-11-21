package jasyncapicmp.model.openapi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Header {
	private String description;
	private Schema schema;
	private boolean required;
	private boolean deprecated;
}
