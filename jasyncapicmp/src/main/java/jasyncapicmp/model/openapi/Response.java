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
public class Response implements Model {
	private String description;
	private Map<String, Header> headers = new HashMap<>();
	private Map<String, MediaType> content = new HashMap<>();
	private Map<String, Link> links = new HashMap<>();
}
