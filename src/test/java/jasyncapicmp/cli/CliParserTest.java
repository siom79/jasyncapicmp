package jasyncapicmp.cli;

import jasyncapicmp.JAsyncApiCmpUserException;
import jasyncapicmp.config.Config;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CliParserTest {

    @Test
    void testSimple() {
        CliParser cliParser = new CliParser();

        Config parse = cliParser.parse(new String[]{"-o", "/path/old", "-n", "/path/new"});

        Assertions.assertThat(parse.getNewPath()).isEqualTo("/path/new");
        Assertions.assertThat(parse.getOldPath()).isEqualTo("/path/old");
    }

    @Test
    void testLong() {
        CliParser cliParser = new CliParser();

        Config parse = cliParser.parse(new String[]{"--old", "/path/old", "--new", "/path/new"});

        Assertions.assertThat(parse.getNewPath()).isEqualTo("/path/new");
        Assertions.assertThat(parse.getOldPath()).isEqualTo("/path/old");
    }

    @Test
    void testNoOldPath() {
        CliParser cliParser = new CliParser();

        Exception exception = Assertions.catchException(() -> cliParser.parse(new String[]{"-o", "-n", "/path/new"}));

        Assertions.assertThat(exception).isInstanceOf(JAsyncApiCmpUserException.class);
    }

    @Test
    void testNoOldPathInsteadLongNew() {
        CliParser cliParser = new CliParser();

        Exception exception = Assertions.catchException(() -> cliParser.parse(new String[]{"-o", "--new", "/path/new"}));

        Assertions.assertThat(exception).isInstanceOf(JAsyncApiCmpUserException.class);
    }

    @Test
    void testNoOldPathLast() {
        CliParser cliParser = new CliParser();

        Exception exception = Assertions.catchException(() -> cliParser.parse(new String[]{"-o"}));

        Assertions.assertThat(exception).isInstanceOf(JAsyncApiCmpUserException.class);
    }

    @Test
    void testNoNewPath() {
        CliParser cliParser = new CliParser();

        Exception exception = Assertions.catchException(() -> cliParser.parse(new String[]{"-o", "/path/old", "-n"}));

        Assertions.assertThat(exception).isInstanceOf(JAsyncApiCmpUserException.class);
    }

    @Test
    void testNoNewPathFirst() {
        CliParser cliParser = new CliParser();

        Exception exception = Assertions.catchException(() -> cliParser.parse(new String[]{"-n", "-o", "/path/old"}));

        Assertions.assertThat(exception).isInstanceOf(JAsyncApiCmpUserException.class);
    }
}