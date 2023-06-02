package jasyncapicmp.output;

import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.util.TestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class StdoutYamlOutputTest {

    @Test
    public void newString() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("asyncapi: \"2.6.0\"", "asyncapi: \"2.6.0\"\nid: new");

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("""
                asyncapi: 2.6.0 (===)
                id: new (+++)
                """);
    }

    @Test
    public void removedString() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("asyncapi: 2.6.0\nid: old", "asyncapi: 2.6.0\n");

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("asyncapi: 2.6.0 (===)\nid: old (---)\n");
    }

    @Test
    public void modifiedString() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("id: old", "id: new");

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("id: new (*** old: old)\n");
    }

    @Test
    public void unchangedString() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("id: my-id", "id: my-id");

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("id: my-id (===)\n");
    }

    @Test
    public void newRemovedUnchangedChangedModel() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("""
servers:
  development:
    tags:
      - name: "removed"
        description: "removed desc"
      - name: "unchanged"
        description: "unchanged desc"
      - name: "changed"
        description: "changed desc"
                """, """
servers:
  development:
    tags:
      - name: "new"
        description: "new desc"
      - name: "unchanged"
        description: "unchanged desc"
      - name: "changed"
        description: "changed-to-this desc"
                """);

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("""
servers: (===)
  development: (===)
    tags: (***)
      - name: removed (---)
        description: removed desc (---)
      - name: unchanged (===)
        description: unchanged desc (===)
      - name: changed (===)
        description: changed-to-this desc (*** old: changed desc)
      - name: new (+++)
        description: new desc (+++)
                """);
    }

    @Test
    public void newListElement() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("""
servers:
  development:
    url: localhost:5672
    protocol: amqp
                """, """
servers:
  development:
    url: localhost:5672
    protocol: amqp
    tags:
      - name: "new"
        description: "new desc"
                """);

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("""
servers: (===)
  development: (===)
    protocol: amqp (===)
    url: localhost:5672 (===)
    tags: (+++)
      - name: new (+++)
        description: new desc (+++)
                """);
    }

    @Test
    public void newMapElement() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("""
servers:
                """, """
servers:
  development:
    url: localhost:5672
    protocol: amqp
                """);

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("""
servers: (+++)
  development: (+++)
    url: localhost:5672 (+++)
    protocol: amqp (+++)
                """);
    }

    @Test
    public void removedMapElement() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("""
servers:
  development:
    url: localhost:5672
    protocol: amqp
                """, """
servers:
                """);

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("""
servers: (---)
  development: (---)
    url: localhost:5672 (---)
    protocol: amqp (---)
                """);
    }

    @Test
    public void newStringListElement() {
        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        ObjectDiff objectDiff = TestUtil.compareYaml("""
servers:
  production:
    url: '{username}.gigantic-server.com:{port}/{basePath}'
    description: The production API server
    protocol: secure-mqtt
    variables:
      username:
        # note! no enum here means it is an open value
        default: demo
        description: This value is assigned by the service provider, in this example `gigantic-server.com`
      port:
        enum:
          - '8883'
        default: '8883'
                """, """
servers:
  production:
    url: '{username}.gigantic-server.com:{port}/{basePath}'
    description: The production API server
    protocol: secure-mqtt
    variables:
      username:
        # note! no enum here means it is an open value
        default: demo
        description: This value is assigned by the service provider, in this example `gigantic-server.com`
      port:
        enum:
          - '8883'
          - '8884'
        default: '8883'
                """);

        outputProcessor.process(objectDiff);
        String output = stdoutOutputTracker.toString();

        Assertions.assertThat(output).isEqualTo("""
servers: (===)
  production: (===)
    protocol: secure-mqtt (===)
    description: The production API server (===)
    url: {username}.gigantic-server.com:{port}/{basePath} (===)
    variables: (===)
      port: (===)
        defaultValue: 8883 (===)
        enums: (***)
          - 8883 (===)
          - 8884 (+++)
      username: (===)
        defaultValue: demo (===)
        description: This value is assigned by the service provider, in this example `gigantic-server.com` (===)
                """);
    }
}