package jasyncapicmp.model.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import jasyncapicmp.model.Model;
import jasyncapicmp.model.Reference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Ref implements Model, Reference {
	@JsonProperty("$ref")
	private String ref;
}
