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
public class MediaType implements Model {
	private Schema schema;
	private Example example;
	private Map<String, Example> examples = new HashMap<>();
	private Map<String, Encoding> encoding = new HashMap<>();
}
