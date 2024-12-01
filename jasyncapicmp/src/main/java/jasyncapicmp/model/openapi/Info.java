package jasyncapicmp.model.openapi;

import jasyncapicmp.model.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Info implements Model {
	private String title;
	private String summary;
	private String description;
	private String termsOfService;
	private Contact contact;
	private License license;
	private String version;
}
