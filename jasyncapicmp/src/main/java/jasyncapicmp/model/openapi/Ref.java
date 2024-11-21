package jasyncapicmp.model.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Ref {
	@JsonProperty("$ref")
	private String ref;
}
