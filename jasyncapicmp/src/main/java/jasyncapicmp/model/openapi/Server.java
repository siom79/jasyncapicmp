package jasyncapicmp.model.openapi;

import jasyncapicmp.model.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class Server implements Model {
	private String url;
	private String description;
	private Map<String, ServerVariable> variables = new HashMap<>();
}
