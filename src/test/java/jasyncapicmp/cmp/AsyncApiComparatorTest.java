package jasyncapicmp.cmp;

import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.model.AsyncApi;
import jasyncapicmp.output.OutputProcessor;
import jasyncapicmp.output.StdoutOutputSink;
import jasyncapicmp.parser.AsyncApiParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class AsyncApiComparatorTest {

    @Test
    void testServerAddedRemovedChanged() {
        AsyncApiComparator comparator = new AsyncApiComparator();
        AsyncApiParser parser = new AsyncApiParser();
        AsyncApi oldApi = parser.parse(("""
                asyncapi: '2.6.0'
                info:
                  title: "Test"
                  version: "1.0.0"
                servers:
                  removed:
                    url: removed-server.example.com
                    protocol: amqp
                  changed:
                    url: changed-server.example.com
                    protocol: kafka
                  unchanged:
                    url: unchanged-server.example.com
                    protocol: amqp""").getBytes(StandardCharsets.UTF_8), "/path");
        AsyncApi newApi = parser.parse(("""
                asyncapi: '2.6.0'
                info:
                  title: "Test"
                  version: "1.0.0"
                servers:
                  new:
                    url: new-server.example.com
                    protocol: amqp
                  changed:
                    url: changed-server.example.com
                    protocol: kafka
                  unchanged:
                    url: unchanged-server.example.com
                    protocol: amqp""").getBytes(StandardCharsets.UTF_8), "/path");

        ObjectDiff objectDiff = comparator.compare(oldApi, newApi);

        Assertions.assertThat(objectDiff.getMapDiffs().get("servers").getMapDiffEntries().get("new").getChangeStatus()).isEqualTo(ChangeStatus.ADDED);
        Assertions.assertThat(objectDiff.getMapDiffs().get("servers").getMapDiffEntries().get("changed").getChangeStatus()).isEqualTo(ChangeStatus.UNCHANGED);
        Assertions.assertThat(objectDiff.getMapDiffs().get("servers").getMapDiffEntries().get("unchanged").getChangeStatus()).isEqualTo(ChangeStatus.UNCHANGED);
        Assertions.assertThat(objectDiff.getMapDiffs().get("servers").getMapDiffEntries().get("removed").getChangeStatus()).isEqualTo(ChangeStatus.REMOVED);
    }

    @Test
    void testListTags() {
        AsyncApiComparator comparator = new AsyncApiComparator();
        AsyncApiParser parser = new AsyncApiParser();
        AsyncApi oldApi = parser.parse(("""
                asyncapi: '2.6.0'
                info:
                  title: "Test"
                  version: "1.0.0"
                servers:
                  my-server:
                    url: my-server.example.com
                    protocol: amqp
                    tags:
                      - name: my-server
                        description: desc-my-server
                      - name: amqp""").getBytes(StandardCharsets.UTF_8), "/path");
        AsyncApi newApi = parser.parse(("""
                asyncapi: '2.6.0'
                info:
                  title: "Test"
                  version: "1.0.0"
                servers:
                  my-server:
                    url: my-server.example.com
                    protocol: amqp
                    tags:
                      - name: example-server
                        description: desc-changed-my-server
                      - name: amqp""").getBytes(StandardCharsets.UTF_8), "/path");

        ObjectDiff objectDiff = comparator.compare(oldApi, newApi);
        Assertions.assertThat(objectDiff.getMapDiffs().get("servers").getMapDiffEntries().get("my-server").getObjectDiff().getListDiffs().get("tags").getListDiffsEntries().size()).isEqualTo(3);

        StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
        OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
        outputProcessor.process(objectDiff);
    }
}