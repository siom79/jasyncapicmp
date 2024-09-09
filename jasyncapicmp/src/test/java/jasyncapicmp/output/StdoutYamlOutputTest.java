package jasyncapicmp.output;

import jasyncapicmp.cmp.ApiCompatibilityCheck;
import jasyncapicmp.cmp.AsyncApiComparator;
import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.model.AsyncApi;
import jasyncapicmp.parser.AsyncApiParser;
import jasyncapicmp.util.TestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class StdoutYamlOutputTest {

    @Test
    public void newString() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("asyncapi: \"2.6.0\"", "asyncapi: \"2.6.0\"\nid: new");

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("asyncapi: 2.6.0 # ===\n" +
				"id: new # +++\n");
    }

    @Test
    public void removedString() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("asyncapi: 2.6.0\nid: old", "asyncapi: 2.6.0\n");

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("asyncapi: 2.6.0 # ===\nid: old # ---\n");
    }

    @Test
    public void modifiedString() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("id: old", "id: new");

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("id: new # *** old: old\n");
    }

    @Test
    public void unchangedString() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("id: my-id", "id: my-id");

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("id: my-id # ===\n");
    }

    @Test
    public void newRemovedUnchangedChangedModel() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("servers:\n" +
				"  development:\n" +
				"    tags:\n" +
				"      - name: \"removed\"\n" +
				"        description: \"removed desc\"\n" +
				"      - name: \"unchanged\"\n" +
				"        description: \"unchanged desc\"\n" +
				"      - name: \"changed\"\n" +
				"        description: \"changed desc\"", "servers:\n" +
				"  development:\n" +
				"    tags:\n" +
				"      - name: \"new\"\n" +
				"        description: \"new desc\"\n" +
				"      - name: \"unchanged\"\n" +
				"        description: \"unchanged desc\"\n" +
				"      - name: \"changed\"\n" +
				"        description: \"changed-to-this desc\"");

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("servers: # ===\n" +
				"  development: # ===\n" +
				"    tags: # ===\n" +
				"      - name: removed # ---\n" +
				"        description: removed desc # ---\n" +
				"      - name: unchanged # ===\n" +
				"        description: unchanged desc # ===\n" +
				"      - name: changed # ===\n" +
				"        description: changed-to-this desc # *** old: changed desc\n" +
				"      - name: new # +++\n" +
				"        description: new desc # +++\n");
    }

    @Test
    public void newListElement() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("servers:\n" +
				"  development:\n" +
				"    url: localhost:5672\n" +
				"    protocol: amqp",
				"servers:\n" +
				"  development:\n" +
				"    url: localhost:5672\n" +
				"    protocol: amqp\n" +
				"    tags:\n" +
				"      - name: \"new\"\n" +
				"        description: \"new desc\"");

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("servers: # ===\n" +
				"  development: # ===\n" +
				"    protocol: amqp # ===\n" +
				"    url: localhost:5672 # ===\n" +
				"    tags: # +++\n" +
				"      - name: new # +++\n" +
				"        description: new desc # +++\n");
    }

    @Test
    public void newMapElement() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("servers:", "servers:\n" +
				"  development:\n" +
				"    url: localhost:5672\n" +
				"    protocol: amqp");

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("servers: # +++\n" +
				"  development: # +++\n" +
				"    url: localhost:5672 # +++\n" +
				"    protocol: amqp # +++\n");
    }

    @Test
    public void removedMapElement() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("servers:\n" +
				"  development:\n" +
				"    url: localhost:5672\n" +
				"    protocol: amqp", "servers:");

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("servers: # ---\n" +
				"  development: # ---\n" +
				"    url: localhost:5672 # ---\n" +
				"    protocol: amqp # ---\n");
    }

    @Test
    public void newStringListElement() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("servers:\n" +
				"  production:\n" +
				"    url: '{username}.gigantic-server.com:{port}/{basePath}'\n" +
				"    description: The production API server\n" +
				"    protocol: secure-mqtt\n" +
				"    variables:\n" +
				"      username:\n" +
				"        # note! no enum here means it is an open value\n" +
				"        default: demo\n" +
				"        description: This value is assigned by the service provider, in this example `gigantic-server.com`\n" +
				"      port:\n" +
				"        enum:\n" +
				"          - '8883'\n" +
				"        default: '8883'"
                , "servers:\n" +
						"  production:\n" +
						"    url: '{username}.gigantic-server.com:{port}/{basePath}'\n" +
						"    description: The production API server\n" +
						"    protocol: secure-mqtt\n" +
						"    variables:\n" +
						"      username:\n" +
						"        # note! no enum here means it is an open value\n" +
						"        default: demo\n" +
						"        description: This value is assigned by the service provider, in this example `gigantic-server.com`\n" +
						"      port:\n" +
						"        enum:\n" +
						"          - '8883'\n" +
						"          - '8884'\n" +
						"        default: '8883'");

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("servers: # ===\n" +
				"  production: # ===\n" +
				"    protocol: secure-mqtt # ===\n" +
				"    description: The production API server # ===\n" +
				"    url: {username}.gigantic-server.com:{port}/{basePath} # ===\n" +
				"    variables: # ===\n" +
				"      port: # ===\n" +
				"        defaultValue: 8883 # ===\n" +
				"        enums: # ===\n" +
				"          - 8883 # ===\n" +
				"          - 8884 # +++\n" +
				"      username: # ===\n" +
				"        defaultValue: demo # ===\n" +
				"        description: This value is assigned by the service provider, in this example `gigantic-server.com` # ===\n");
    }

	@Test
	void testSchemaOneOfRef() {
		AsyncApiComparator comparator = new AsyncApiComparator();
		AsyncApiParser parser = new AsyncApiParser();
		AsyncApi oldApi = parser.parse(("channels:\n" +
				"  userSignedUp:\n" +
				"    subscribe:\n" +
				"      operationId: userSignup\n" +
				"      summary: Action to sign a user up.\n" +
				"      description: A longer description\n" +
				"      message:\n" +
				"        oneOf:\n" +
				"          - $ref: \"#/components/messages/M1\"\n" +
				"          - $ref: \"#/components/messages/M2\"\n" +
				"components:\n" +
				"  messages:\n" +
				"    M1:\n" +
				"      title: \"Title1\"\n" +
				"    M2:\n" +
				"      title: \"Title2\"").getBytes(StandardCharsets.UTF_8), "/path");
		AsyncApi newApi = parser.parse(("channels:\n" +
				"  userSignedUp:\n" +
				"    subscribe:\n" +
				"      operationId: userSignup\n" +
				"      summary: Action to sign a user up.\n" +
				"      description: A longer description\n" +
				"      message:\n" +
				"        oneOf:\n" +
				"          - $ref: \"#/components/messages/M1\"\n" +
				"          - $ref: \"#/components/messages/M2\"\n" +
				"components:\n" +
				"  messages:\n" +
				"    M1:\n" +
				"      title: \"Title1\"\n" +
				"    M2:\n" +
				"      title: \"Title2\"").getBytes(StandardCharsets.UTF_8), "/path");

		ObjectDiff objectDiff = comparator.compare(oldApi, newApi);
		ApiCompatibilityCheck apiCompatibilityCheck = new ApiCompatibilityCheck();
		apiCompatibilityCheck.check(objectDiff);

		StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
		OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
		outputProcessor.process(objectDiff);
		String output = stdoutOutputTracker.toString();

		Assertions.assertThat(output).isEqualTo("channels: # ===\n" +
				"  userSignedUp: # ===\n" +
				"    subscribe: # ===\n" +
				"      summary: Action to sign a user up. # ===\n" +
				"      operationId: userSignup # ===\n" +
				"      description: A longer description # ===\n" +
				"      message: # ===\n" +
				"        oneOf: # ===\n" +
				"          - ref: #/components/messages/M1 # ===\n" +
				"          - ref: #/components/messages/M2 # ===\n" +
				"components: # ===\n" +
				"  messages: # ===\n" +
				"    M1: # ===\n" +
				"      title: Title1 # ===\n" +
				"    M2: # ===\n" +
				"      title: Title2 # ===\n");
	}

	@Test
	void testInvalidYaml() {
		AsyncApiComparator comparator = new AsyncApiComparator();
		AsyncApiParser parser = new AsyncApiParser();
		AsyncApi oldApi = parser.parse(("services:\n" +
			"\n" +
			"  frontend:\n" +
			"    build:\n" +
			"      context: ./frontend\n" +
			"    container_name: japicmpweb-frontend\n" +
			"    ports:\n" +
			"      - \"80:80\"\n" +
			"      - \"443:443\"").getBytes(StandardCharsets.UTF_8), "/path");
		AsyncApi newApi = parser.parse(("services:\n" +
			"\n" +
			"  frontend:\n" +
			"    build:\n" +
			"      context: ./frontend\n" +
			"    container_name: japicmpweb-frontend\n" +
			"    ports:\n" +
			"      - \"80:80\"\n" +
			"      - \"443:443\"").getBytes(StandardCharsets.UTF_8), "/path");

		ObjectDiff objectDiff = comparator.compare(oldApi, newApi);
		System.out.println(objectDiff);
	}
}
