package jasyncapicmp.parser;

import jasyncapicmp.model.openapi.OpenApi;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpenApiParserTest {

	@Test
	void testOpenApi() throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "openapi.yaml"));
		ApiParser parser = new ApiParser();
		OpenApi api = (OpenApi) parser.parse(bytes, "/path");

		Assertions.assertThat(api.getOpenapi()).isEqualTo("3.1.1");

		Assertions.assertThat(api.getInfo().getTitle()).isEqualTo("Example Pet Store App");
		Assertions.assertThat(api.getInfo().getSummary()).isEqualTo("A pet store manager.");
		Assertions.assertThat(api.getInfo().getDescription()).isEqualTo("This is an example server for a pet store.");
		Assertions.assertThat(api.getInfo().getTermsOfService()).isEqualTo("https://example.com/terms/");
		Assertions.assertThat(api.getInfo().getContact().getName()).isEqualTo("API Support");
		Assertions.assertThat(api.getInfo().getContact().getUrl()).isEqualTo("https://www.example.com/support");
		Assertions.assertThat(api.getInfo().getContact().getEmail()).isEqualTo("support@example.com");
		Assertions.assertThat(api.getInfo().getLicense().getName()).isEqualTo("Apache 2.0");
		Assertions.assertThat(api.getInfo().getLicense().getUrl()).isEqualTo("https://www.apache.org/licenses/LICENSE-2.0.html");
		Assertions.assertThat(api.getInfo().getVersion()).isEqualTo("1.0.1");

		Assertions.assertThat(api.getServers().size()).isEqualTo(2);
		Assertions.assertThat(api.getServers().get(0).getUrl()).isEqualTo("https://development.gigantic-server.com/v1");
		Assertions.assertThat(api.getServers().get(0).getDescription()).isEqualTo("Development server");
		Assertions.assertThat(api.getServers().get(1).getUrl()).isEqualTo("https://{username}.gigantic-server.com:{port}/{basePath}");
		Assertions.assertThat(api.getServers().get(1).getDescription()).isEqualTo("The production API server");
		Assertions.assertThat(api.getServers().get(1).getVariables().get("username").getDefaultProperty()).isEqualTo("demo");
		Assertions.assertThat(api.getServers().get(1).getVariables().get("username").getDescription()).isEqualTo("A user-specific subdomain. Use `demo` for a free sandbox environment.");
		Assertions.assertThat(api.getServers().get(1).getVariables().get("port").getEnumProperty().get(0)).isEqualTo("8443");
		Assertions.assertThat(api.getServers().get(1).getVariables().get("port").getEnumProperty().get(1)).isEqualTo("443");
		Assertions.assertThat(api.getServers().get(1).getVariables().get("port").getDefaultProperty()).isEqualTo("8443");
		Assertions.assertThat(api.getServers().get(1).getVariables().get("basePath").getDefaultProperty()).isEqualTo("v2");

		Assertions.assertThat(api.getPaths().size()).isEqualTo(1);
		Assertions.assertThat(api.getPaths().get("/pets").getGet().getDescription()).isEqualTo("Returns pets based on ID");
		Assertions.assertThat(api.getPaths().get("/pets").getGet().getSummary()).isEqualTo("Find pets by ID");
		Assertions.assertThat(api.getPaths().get("/pets").getGet().getOperationId()).isEqualTo("getPetsById");
		Assertions.assertThat(api.getPaths().get("/pets").getGet().getResponses().get("200").getDescription()).isEqualTo("pet response");
		Assertions.assertThat(api.getPaths().get("/pets").getGet().getResponses().get("200").getContent().get("*/*").getSchema().getType()).isEqualTo("array");
		Assertions.assertThat(api.getPaths().get("/pets").getGet().getResponses().get("200").getContent().get("*/*").getSchema().getItems().getRef()).isEqualTo("#/components/schemas/Pet");
		Assertions.assertThat(api.getPaths().get("/pets").getGet().getResponses().get("default").getDescription()).isEqualTo("error payload");
		Assertions.assertThat(api.getPaths().get("/pets").getGet().getResponses().get("default").getContent().get("text/html").getSchema().getRef()).isEqualTo("#/components/schemas/ErrorModel");
		Assertions.assertThat(api.getPaths().get("/pets").getParameters().get(0).getName()).isEqualTo("id");
		Assertions.assertThat(api.getPaths().get("/pets").getParameters().get(0).getIn()).isEqualTo("path");
		Assertions.assertThat(api.getPaths().get("/pets").getParameters().get(0).getDescription()).isEqualTo("ID of pet to use");
		Assertions.assertThat(api.getPaths().get("/pets").getParameters().get(0).isRequired()).isEqualTo(true);
		Assertions.assertThat(api.getPaths().get("/pets").getParameters().get(0).getSchema().getType()).isEqualTo("array");
		Assertions.assertThat(api.getPaths().get("/pets").getParameters().get(0).getSchema().getItems().getType()).isEqualTo("string");
		Assertions.assertThat(api.getPaths().get("/pets").getParameters().get(0).getStyle()).isEqualTo("simple");
	}

}
