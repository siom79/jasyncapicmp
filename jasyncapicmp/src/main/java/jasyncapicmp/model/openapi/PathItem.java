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
public class PathItem {
	@JsonProperty("$ref")
	private String ref;
	private String summary;
	private String description;
	private Operation get;
	private Operation put;
	private Operation post;
	private Operation delete;
	private Operation options;
	private Operation head;
	private Operation patch;
	private Operation trace;
	private List<Server> servers = new ArrayList<>();
	private List<Parameter> parameters = new ArrayList<>();
}
