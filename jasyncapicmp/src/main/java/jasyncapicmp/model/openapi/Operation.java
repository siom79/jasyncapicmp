package jasyncapicmp.model.openapi;

import jasyncapicmp.model.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Operation implements Model {
	private List<String> tags = new ArrayList<>();
	private String summary;
	private String description;
	private ExternalDocumentation externalDocs;
	private String operationId;
	private List<Parameter> parameters = new ArrayList<>();
	private RequestBody requestBody;
	private Map<String, Response> responses = new HashMap<>();
	private Map<String, Callback> callbacks = new HashMap<>();
	private boolean deprecated;
	private Map<String, List<String>> security = new HashMap<>();
	private List<Server> servers = new ArrayList<>();
}
