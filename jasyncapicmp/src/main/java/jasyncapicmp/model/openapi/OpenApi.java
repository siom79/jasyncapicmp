package jasyncapicmp.model.openapi;

import jasyncapicmp.model.Api;
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
public class OpenApi implements Api, Model {
	private String openapi;
	private Info info;
	private String jsonSchemaDialect;
	private List<Server> servers = new ArrayList<>();
	private Map<String, PathItem> paths = new HashMap<>();
	private Map<String, PathItem> webhooks = new HashMap<>();
	private Components components;
	private SecurityRequirement securityRequirement;
	private List<Tag> tags;
	private ExternalDocumentation externalDocs;
}
