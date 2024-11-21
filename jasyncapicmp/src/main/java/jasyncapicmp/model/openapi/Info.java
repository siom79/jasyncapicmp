package jasyncapicmp.model.openapi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Info {
	private String title;
	private String summary;
	private String description;
	private String termsOfService;
	private Contact contact;
	private License license;
	private String version;
}
