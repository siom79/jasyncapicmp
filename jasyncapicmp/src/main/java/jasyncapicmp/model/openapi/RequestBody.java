package jasyncapicmp.model.openapi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class RequestBody {
	private String description;
	private Map<String, MediaType> content = new HashMap<>();
	private boolean required;
}
