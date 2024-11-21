package jasyncapicmp.model.openapi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class Components {
	private Map<String, Schema> schemas = new HashMap<>();
	private Map<String, Response> responses = new HashMap<>();
	private Map<String, Parameter> parameters = new HashMap<>();
	private Map<String, Example> examples = new HashMap<>();
	private Map<String, RequestBody> requestBodies = new HashMap<>();
	private Map<String, Header> headers = new HashMap<>();
	private Map<String, SecurityScheme> securitySchemes = new HashMap<>();
	private Map<String, Link> links = new HashMap<>();
	private Map<String, Callback> callbacks = new HashMap<>();
	private Map<String, PathItem> pathItems = new HashMap<>();
}
