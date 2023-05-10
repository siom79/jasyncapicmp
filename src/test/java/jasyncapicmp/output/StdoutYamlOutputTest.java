package jasyncapicmp.output;

import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.util.TestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class StdoutYamlOutputTest {

    @Test
    public void newString() {
        StdoutYamlOutput stdoutYamlOutput = new StdoutYamlOutput();
        ObjectDiff objectDiff = TestUtil.compareYaml("asyncapi: \"2.6.0\"", "asyncapi: \"2.6.0\"\nid: new");

        String output = stdoutYamlOutput.print(objectDiff);

        Assertions.assertThat(output).isEqualTo("asyncapi: 2.6.0 (===)\n" +
                "id: new (+++)\n");
    }

    @Test
    public void removedString() {
        StdoutYamlOutput stdoutYamlOutput = new StdoutYamlOutput();
        ObjectDiff objectDiff = TestUtil.compareYaml("asyncapi: 2.6.0\nid: old", "asyncapi: 2.6.0\n");

        String output = stdoutYamlOutput.print(objectDiff);

        Assertions.assertThat(output).isEqualTo("asyncapi: 2.6.0 (===)\nid: old (---)\n");
    }

    @Test
    public void modifiedString() {
        StdoutYamlOutput stdoutYamlOutput = new StdoutYamlOutput();
        ObjectDiff objectDiff = TestUtil.compareYaml("id: old", "id: new");

        String output = stdoutYamlOutput.print(objectDiff);

        Assertions.assertThat(output).isEqualTo("id: new (*** old: old)\n");
    }

    @Test
    public void unchangedString() {
        StdoutYamlOutput stdoutYamlOutput = new StdoutYamlOutput();
        ObjectDiff objectDiff = TestUtil.compareYaml("id: my-id", "id: my-id");

        String output = stdoutYamlOutput.print(objectDiff);

        Assertions.assertThat(output).isEqualTo("id: my-id (===)\n");
    }

    @Test
    public void newRemovedUnchangedChangedModel() {
        StdoutYamlOutput stdoutYamlOutput = new StdoutYamlOutput();
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

        String output = stdoutYamlOutput.print(objectDiff);

        Assertions.assertThat(output).isEqualTo("""
servers:
  development:
    tags:
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
        StdoutYamlOutput stdoutYamlOutput = new StdoutYamlOutput();
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

        String output = stdoutYamlOutput.print(objectDiff);

        Assertions.assertThat(output).isEqualTo("""
servers:
  development:
    protocol: amqp (===)
    url: localhost:5672 (===)
    tags:
      - name: new (+++)
        description: new desc (+++)
                """);
    }

    @Test
    public void newMapElement() {
        StdoutYamlOutput stdoutYamlOutput = new StdoutYamlOutput();
        ObjectDiff objectDiff = TestUtil.compareYaml("""
servers:
                """, """
servers:
  development:
    url: localhost:5672
    protocol: amqp
                """);

        String output = stdoutYamlOutput.print(objectDiff);

        Assertions.assertThat(output).isEqualTo("""
servers:
  development:
    url: localhost:5672 (+++)
    protocol: amqp (+++)
                """);
    }

    @Test
    public void removedMapElement() {
        StdoutYamlOutput stdoutYamlOutput = new StdoutYamlOutput();
        ObjectDiff objectDiff = TestUtil.compareYaml("""
servers:
  development:
    url: localhost:5672
    protocol: amqp
                """, """
servers:
                """);

        String output = stdoutYamlOutput.print(objectDiff);

        Assertions.assertThat(output).isEqualTo("""
servers:
  development:
    url: localhost:5672 (---)
    protocol: amqp (---)
                """);
    }
}