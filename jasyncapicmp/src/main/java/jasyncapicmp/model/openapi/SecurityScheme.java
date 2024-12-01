package jasyncapicmp.model.openapi;

import jasyncapicmp.model.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SecurityScheme implements Model {
	private String type;
	private String description;
	private String name;
	private String in;
	private String scheme;
	private String bearerFormat;
	private OAuthFlows flows;
	private String openIdConnectUrl;
}
