package jasyncapicmp.parser;

import jasyncapicmp.JAsyncApiCmpUserException;
import jasyncapicmp.model.AsyncApi;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

class AsyncApiParserTest {

    @Test
    void testJson() {
        AsyncApiParser parser = new AsyncApiParser();
        String content = "{\"asyncapi\":\"3.0.0\"}";

        AsyncApi asyncApi = parser.parse(content.getBytes(StandardCharsets.UTF_8), "/path");

        Assertions.assertThat(asyncApi.getAsyncapi()).isEqualTo("3.0.0");
    }

    @Test
    void testYaml() {
        AsyncApiParser parser = new AsyncApiParser();
        String content = "asyncapi: \"3.0.0\"\n";

        AsyncApi asyncApi = parser.parse(content.getBytes(StandardCharsets.UTF_8), "/path");

        Assertions.assertThat(asyncApi.getAsyncapi()).isEqualTo("3.0.0");
    }

    @Test
    void testEmpty() {
        AsyncApiParser parser = new AsyncApiParser();
        String content = "";

        Exception exception = Assertions.catchException(() -> parser.parse(content.getBytes(StandardCharsets.UTF_8), "/path"));

        Assertions.assertThat(exception).isInstanceOf(JAsyncApiCmpUserException.class);
    }

    @Test
    void parseNullJson() {
        AsyncApiParser parser = new AsyncApiParser();

        Exception exception = Assertions.catchException(() -> parser.parseJson(null, "/path"));

        Assertions.assertThat(exception).isInstanceOf(JAsyncApiCmpUserException.class);
    }

    @Test
    void parseNullYaml() {
        AsyncApiParser parser = new AsyncApiParser();

        Exception exception = Assertions.catchException(() -> parser.parseYaml(null, "/path"));

        Assertions.assertThat(exception).isInstanceOf(JAsyncApiCmpUserException.class);
    }

    @Test
    void testSample2_6_0() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "new_2.6.0.yaml"));
        AsyncApiParser parser = new AsyncApiParser();

        AsyncApi asyncApi = parser.parse(bytes, "/path");

        Assertions.assertThat(asyncApi.getAsyncapi()).isEqualTo("2.6.0");
        Assertions.assertThat(asyncApi.getId()).isEqualTo("https://github.com/siom79/jasyncapicmp");
        Assertions.assertThat(asyncApi.getDefaultContentType()).isEqualTo("application/json");

        Assertions.assertThat(asyncApi.getInfo().getTitle()).isEqualTo("AsyncAPI Sample App");
        Assertions.assertThat(asyncApi.getInfo().getVersion()).isEqualTo("1.0.0");
        Assertions.assertThat(asyncApi.getInfo().getDescription()).isEqualTo("This is a sample app.");
        Assertions.assertThat(asyncApi.getInfo().getTermsOfService()).isEqualTo("https://asyncapi.org/terms/");
        Assertions.assertThat(asyncApi.getInfo().getContact().getName()).isEqualTo("API Support");
        Assertions.assertThat(asyncApi.getInfo().getContact().getUrl()).isEqualTo("https://www.asyncapi.org/support");
        Assertions.assertThat(asyncApi.getInfo().getContact().getEmail()).isEqualTo("support@asyncapi.org");
        Assertions.assertThat(asyncApi.getInfo().getLicense().getName()).isEqualTo("Apache 2.0");
        Assertions.assertThat(asyncApi.getInfo().getLicense().getUrl()).isEqualTo("https://www.apache.org/licenses/LICENSE-2.0.html");

        Assertions.assertThat(asyncApi.getServers().get("development").getUrl()).isEqualTo("localhost:5672");
        Assertions.assertThat(asyncApi.getServers().get("development").getDescription()).isEqualTo("Development AMQP broker.");
        Assertions.assertThat(asyncApi.getServers().get("development").getProtocol()).isEqualTo("amqp");
        Assertions.assertThat(asyncApi.getServers().get("development").getProtocolVersion()).isEqualTo("0-9-1");
        Assertions.assertThat(asyncApi.getServers().get("development").getTags().get(0).getName()).isEqualTo("env:development");
        Assertions.assertThat(asyncApi.getServers().get("development").getTags().get(0).getDescription()).isEqualTo("This environment is meant for developers to run their own tests.");
        Assertions.assertThat(asyncApi.getServers().get("staging").getVariables().get("env").getDescription()).isEqualTo("Environment to connect to. It can be either `production` or `staging`.");
        Assertions.assertThat(asyncApi.getServers().get("staging").getVariables().get("env").getEnums().get(0)).isEqualTo("production");
        Assertions.assertThat(asyncApi.getServers().get("staging").getVariables().get("env").getEnums().get(1)).isEqualTo("staging");

        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getOperationId()).isEqualTo("userSignup");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getSummary()).isEqualTo("Action to sign a user up.");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getDescription()).isEqualTo("A longer description");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getContentType()).isEqualTo("application/json");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getTags().get(0).getName()).isEqualTo("user");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getTags().get(1).getName()).isEqualTo("signup");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getTags().get(2).getName()).isEqualTo("register");

        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getHeaders().getType()).isEqualTo("object");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getHeaders().getProperties().get("correlationId").getDescription()).isEqualTo("Correlation ID set by application");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getHeaders().getProperties().get("correlationId").getType()).isEqualTo("string");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getHeaders().getProperties().get("applicationInstanceId").getDescription()).isEqualTo("Unique identifier for a given instance of the publishing application");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getHeaders().getProperties().get("applicationInstanceId").getType()).isEqualTo("string");

        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getPayload().getType()).isEqualTo("object");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getPayload().getProperties().get("user").getType()).isEqualTo("object");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getPayload().getProperties().get("signup").getType()).isEqualTo("object");

        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getCorrelationId().getDescription()).isEqualTo("Default Correlation ID");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getCorrelationId().getLocation()).isEqualTo("$message.header#/correlationId");

        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getTraits().get(0).getRef()).isEqualTo("#/components/messageTraits/commonHeaders");

        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getExamples().get(0).getName()).isEqualTo("SimpleSignup");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getSubscribe().getMessage().getExamples().get(0).getSummary()).isEqualTo("A simple UserSignup example message");

        Assertions.assertThat(asyncApi.getComponents().getSchemas().get("Category").getType()).isEqualTo("object");
        Assertions.assertThat(asyncApi.getComponents().getSchemas().get("Category").getProperties().get("id").getType()).isEqualTo("integer");
        Assertions.assertThat(asyncApi.getComponents().getSchemas().get("Category").getProperties().get("id").getFormat()).isEqualTo("int64");
        Assertions.assertThat(asyncApi.getComponents().getSchemas().get("Category").getProperties().get("name").getType()).isEqualTo("string");

        Assertions.assertThat(asyncApi.getComponents().getServers().get("development").getDescription()).isEqualTo("RabbitMQ broker");
        Assertions.assertThat(asyncApi.getComponents().getServers().get("development").getProtocol()).isEqualTo("amqp");
        Assertions.assertThat(asyncApi.getComponents().getServers().get("development").getProtocolVersion()).isEqualTo("0-9-1");
        Assertions.assertThat(asyncApi.getComponents().getServers().get("development").getVariables().get("stage").getRef()).isEqualTo("#/components/serverVariables/stage");
        Assertions.assertThat(asyncApi.getComponents().getServers().get("development").getVariables().get("port").getRef()).isEqualTo("#/components/serverVariables/port");

        Assertions.assertThat(asyncApi.getComponents().getServerVariables().get("stage").getDefaultValue()).isEqualTo("demo");
        Assertions.assertThat(asyncApi.getComponents().getServerVariables().get("stage").getDescription()).isEqualTo("This value is assigned by the service provider, in this example `mycompany.com`");
        Assertions.assertThat(asyncApi.getComponents().getServerVariables().get("port").getDefaultValue()).isEqualTo("5672");

        Assertions.assertThat(asyncApi.getComponents().getChannels().get("user/signedup").getSubscribe().getMessage().getRef()).isEqualTo("#/components/messages/userSignUp");

        Assertions.assertThat(asyncApi.getComponents().getMessages().get("userSignUp").getSummary()).isEqualTo("Action to sign a user up.");
        Assertions.assertThat(asyncApi.getComponents().getMessages().get("userSignUp").getDescription()).isEqualTo("Multiline description of what this action does.\n" +
                "Here you have another line.\n");
        Assertions.assertThat(asyncApi.getComponents().getMessages().get("userSignUp").getTags().get(0).getName()).isEqualTo("user");
        Assertions.assertThat(asyncApi.getComponents().getMessages().get("userSignUp").getTags().get(1).getName()).isEqualTo("signup");
        Assertions.assertThat(asyncApi.getComponents().getMessages().get("userSignUp").getHeaders().getType()).isEqualTo("object");
        Assertions.assertThat(asyncApi.getComponents().getMessages().get("userSignUp").getHeaders().getProperties().get("applicationInstanceId").getDescription()).isEqualTo("Unique identifier for a given instance of the publishing application");
        Assertions.assertThat(asyncApi.getComponents().getMessages().get("userSignUp").getHeaders().getProperties().get("applicationInstanceId").getType()).isEqualTo("string");
        Assertions.assertThat(asyncApi.getComponents().getMessages().get("userSignUp").getPayload().getType()).isEqualTo("object");
        Assertions.assertThat(asyncApi.getComponents().getMessages().get("userSignUp").getPayload().getProperties().get("user").getType()).isEqualTo("object");
        Assertions.assertThat(asyncApi.getComponents().getMessages().get("userSignUp").getPayload().getProperties().get("signup").getType()).isEqualTo("object");

        Assertions.assertThat(asyncApi.getComponents().getParameters().get("userId").getDescription()).isEqualTo("Id of the user.");
        Assertions.assertThat(asyncApi.getComponents().getParameters().get("userId").getSchema().getType()).isEqualTo("string");

        Assertions.assertThat(asyncApi.getComponents().getCorrelationIds().get("default").getDescription()).isEqualTo("Default Correlation ID");
        Assertions.assertThat(asyncApi.getComponents().getCorrelationIds().get("default").getLocation()).isEqualTo("$message.header#/correlationId");

        Assertions.assertThat(asyncApi.getComponents().getMessageTraits().get("commonHeaders").getHeaders().getType()).isEqualTo("object");
        Assertions.assertThat(asyncApi.getComponents().getMessageTraits().get("commonHeaders").getHeaders().getProperties().get("my-app-header").getType()).isEqualTo("integer");
        Assertions.assertThat(asyncApi.getComponents().getMessageTraits().get("commonHeaders").getHeaders().getProperties().get("my-app-header").getMinimum()).isEqualTo(0);
        Assertions.assertThat(asyncApi.getComponents().getMessageTraits().get("commonHeaders").getHeaders().getProperties().get("my-app-header").getMaximum()).isEqualTo(100);
    }

    @Test
    void testSample3_0_0() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "new_3.0.0.yaml"));
        AsyncApiParser parser = new AsyncApiParser();

        AsyncApi asyncApi = parser.parse(bytes, "/path");

        Assertions.assertThat(asyncApi.getAsyncapi()).isEqualTo("3.0.0");
        Assertions.assertThat(asyncApi.getId()).isEqualTo("https://github.com/siom79/jasyncapicmp");
        Assertions.assertThat(asyncApi.getDefaultContentType()).isEqualTo("application/json");

        Assertions.assertThat(asyncApi.getInfo().getTitle()).isEqualTo("AsyncAPI Sample App");
        Assertions.assertThat(asyncApi.getInfo().getVersion()).isEqualTo("1.0.1");
        Assertions.assertThat(asyncApi.getInfo().getDescription()).isEqualTo("This is a sample app.");
        Assertions.assertThat(asyncApi.getInfo().getTermsOfService()).isEqualTo("https://asyncapi.org/terms/");
        Assertions.assertThat(asyncApi.getInfo().getContact().getName()).isEqualTo("API Support");
        Assertions.assertThat(asyncApi.getInfo().getContact().getUrl()).isEqualTo("https://www.asyncapi.org/support");
        Assertions.assertThat(asyncApi.getInfo().getContact().getEmail()).isEqualTo("support@asyncapi.org");
        Assertions.assertThat(asyncApi.getInfo().getLicense().getName()).isEqualTo("Apache 2.0");
        Assertions.assertThat(asyncApi.getInfo().getLicense().getUrl()).isEqualTo("https://www.apache.org/licenses/LICENSE-2.0.html");
        Assertions.assertThat(asyncApi.getInfo().getExternalDocs().getDescription()).isEqualTo("Find more info here");
        Assertions.assertThat(asyncApi.getInfo().getExternalDocs().getUrl()).isEqualTo("https://www.asyncapi.org");
        Assertions.assertThat(asyncApi.getInfo().getTags().get(0).getName()).isEqualTo("e-commerce");

        Assertions.assertThat(asyncApi.getServers().get("development").getHost()).isEqualTo("localhost:5672");
        Assertions.assertThat(asyncApi.getServers().get("development").getDescription()).isEqualTo("Development AMQP broker.");
        Assertions.assertThat(asyncApi.getServers().get("development").getProtocol()).isEqualTo("amqp");
        Assertions.assertThat(asyncApi.getServers().get("development").getProtocolVersion()).isEqualTo("0-9-1");
        Assertions.assertThat(asyncApi.getServers().get("development").getTags().get(0).getName()).isEqualTo("env:development");
        Assertions.assertThat(asyncApi.getServers().get("development").getTags().get(0).getDescription()).isEqualTo("This environment is meant for developers to run their own tests.");
        Assertions.assertThat(asyncApi.getServers().get("staging").getPathname()).isEqualTo("/{env}");
        Assertions.assertThat(asyncApi.getServers().get("staging").getVariables().get("env").getDescription()).isEqualTo("Environment to connect to. It can be either `production` or `staging`.");
        Assertions.assertThat(asyncApi.getServers().get("staging").getVariables().get("env").getEnums().get(0)).isEqualTo("production");
        Assertions.assertThat(asyncApi.getServers().get("staging").getVariables().get("env").getEnums().get(1)).isEqualTo("staging");

        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getAddress()).isEqualTo("user.signedup");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getMessageId()).isEqualTo("userSignup");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getName()).isEqualTo("UserSignup");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getTitle()).isEqualTo("User signup");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getSummary()).isEqualTo("Action to sign a user up.");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getDescription()).isEqualTo("A longer description");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getContentType()).isEqualTo("application/json");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getTags().get(0).getName()).isEqualTo("user");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getTags().get(1).getName()).isEqualTo("signup");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getTags().get(2).getName()).isEqualTo("register");

        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getHeaders().getType()).isEqualTo("object");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getHeaders().getProperties().get("correlationId").getDescription()).isEqualTo("Correlation ID set by application");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getHeaders().getProperties().get("correlationId").getType()).isEqualTo("string");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getHeaders().getProperties().get("applicationInstanceId").getDescription()).isEqualTo("Unique identifier for a given instance of the publishing application");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getHeaders().getProperties().get("applicationInstanceId").getType()).isEqualTo("string");

        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getPayload().getType()).isEqualTo("object");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getPayload().getProperties().get("user").getRef()).isEqualTo("#/components/schemas/userCreate");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getPayload().getProperties().get("signup").getRef()).isEqualTo("#/components/schemas/signup");

        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getCorrelationId().getDescription()).isEqualTo("Default Correlation ID");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getCorrelationId().getLocation()).isEqualTo("$message.header#/correlationId");

        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getTraits().get(0).getRef()).isEqualTo("#/components/messageTraits/commonHeaders");

        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getExamples().get(0).getName()).isEqualTo("SimpleSignup");
        Assertions.assertThat(asyncApi.getChannels().get("userSignedUp").getMessages().get("userSignedUp").getExamples().get(0).getSummary()).isEqualTo("A simple UserSignup example message");

        Assertions.assertThat(asyncApi.getOperations().get("onUserSignUp").getTitle()).isEqualTo("User sign up");
        Assertions.assertThat(asyncApi.getOperations().get("onUserSignUp").getSummary()).isEqualTo("Action to sign a user up.");
        Assertions.assertThat(asyncApi.getOperations().get("onUserSignUp").getDescription()).isEqualTo("A longer description");
        Assertions.assertThat(asyncApi.getOperations().get("onUserSignUp").getChannel().getRef()).isEqualTo("#/channels/userSignedUp");
        Assertions.assertThat(asyncApi.getOperations().get("onUserSignUp").getAction()).isEqualTo("send");
        Assertions.assertThat(asyncApi.getOperations().get("onUserSignUp").getTags().get(0).getName()).isEqualTo("user");
        Assertions.assertThat(asyncApi.getOperations().get("onUserSignUp").getBindings().getKafka().getBindingVersion()).isEqualTo("1.0.0");
        Assertions.assertThat(asyncApi.getOperations().get("onUserSignUp").getTraits().get(0).getRef()).isEqualTo("#/components/operationTraits/kafka");

        Assertions.assertThat(asyncApi.getComponents().getSchemas().get("userCreate").getType()).isEqualTo("object");
        Assertions.assertThat(asyncApi.getComponents().getSchemas().get("userCreate").getProperties().get("id").getType()).isEqualTo("integer");
        Assertions.assertThat(asyncApi.getComponents().getSchemas().get("userCreate").getProperties().get("id").getFormat()).isEqualTo("int64");
        Assertions.assertThat(asyncApi.getComponents().getSchemas().get("userCreate").getProperties().get("name").getType()).isEqualTo("string");

        Assertions.assertThat(asyncApi.getComponents().getServers().get("development").getDescription()).isEqualTo("RabbitMQ broker");
        Assertions.assertThat(asyncApi.getComponents().getServers().get("development").getProtocol()).isEqualTo("amqp");
        Assertions.assertThat(asyncApi.getComponents().getServers().get("development").getProtocolVersion()).isEqualTo("0-9-1");
        Assertions.assertThat(asyncApi.getComponents().getServers().get("development").getVariables().get("stage").getRef()).isEqualTo("#/components/serverVariables/stage");
        Assertions.assertThat(asyncApi.getComponents().getServers().get("development").getVariables().get("port").getRef()).isEqualTo("#/components/serverVariables/port");

        Assertions.assertThat(asyncApi.getComponents().getServerVariables().get("stage").getDefaultValue()).isEqualTo("demo");
        Assertions.assertThat(asyncApi.getComponents().getServerVariables().get("stage").getDescription()).isEqualTo("This value is assigned by the service provider, in this example `mycompany.com`");
        Assertions.assertThat(asyncApi.getComponents().getServerVariables().get("port").getEnums().get(0)).isEqualTo("5671");
        Assertions.assertThat(asyncApi.getComponents().getServerVariables().get("port").getEnums().get(1)).isEqualTo("5672");
        Assertions.assertThat(asyncApi.getComponents().getServerVariables().get("port").getDefaultValue()).isEqualTo("5672");
    }

    @Test
    void testMessageOneOf() {
        AsyncApiParser parser = new AsyncApiParser();
        AsyncApi asyncApi = parser.parse(("{\n" +
                "  \"channels\": {\n" +
                "    \"mychannel\": {\n" +
                "      \"subscribe\": {\n" +
                "        \"message\": {\n" +
                "          \"oneOf\": [\n" +
                "            {\n" +
                "              \"$ref\": \"#/components/messages/signup\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"$ref\": \"#/components/messages/login\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"components\": {\n" +
                "    \"messages\": {\n" +
                "      \"signup\": {\n" +
                "        \"title\": \"signup\",\n" +
                "        \"type\": \"object\"\n" +
                "      },\n" +
                "      \"login\": {\n" +
                "        \"title\": \"login\",\n" +
                "        \"type\": \"object\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}").getBytes(StandardCharsets.UTF_8), "/path");

        Assertions.assertThat(asyncApi.getChannels().get("mychannel").getSubscribe().getMessage().getOneOf().get(0).getTitle()).isEqualTo("signup");
        Assertions.assertThat(asyncApi.getChannels().get("mychannel").getSubscribe().getMessage().getOneOf().get(1).getTitle()).isEqualTo("login");
    }
}