package jasyncapicmp.model.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ServerVariable {
	@JsonProperty("enum")
	private List<String> enumProperty = new ArrayList<>();
	@JsonProperty("default")
	private String defaultProperty;
	private String description;
}
