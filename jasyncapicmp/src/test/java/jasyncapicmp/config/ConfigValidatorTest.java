package jasyncapicmp.config;

import jasyncapicmp.JAsyncApiCmpUserException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ConfigValidatorTest {

    @Test
    void testNoOldPath() {
        Config config = new Config();
        config.setNewPath("/path");
        ConfigValidator configValidator = new ConfigValidator();

        Exception exception = Assertions.catchException(() -> configValidator.validate(config));

        Assertions.assertThat(exception).isInstanceOf(JAsyncApiCmpUserException.class);
    }

    @Test
    void testEmptyOldPath() {
        Config config = new Config();
        config.setNewPath("");
        ConfigValidator configValidator = new ConfigValidator();

        Exception exception = Assertions.catchException(() -> configValidator.validate(config));

        Assertions.assertThat(exception).isInstanceOf(JAsyncApiCmpUserException.class);
    }

    @Test
    void testNoNewPath() {
        Config config = new Config();
        config.setOldPath("/path");
        ConfigValidator configValidator = new ConfigValidator();

        Exception exception = Assertions.catchException(() -> configValidator.validate(config));

        Assertions.assertThat(exception).isInstanceOf(JAsyncApiCmpUserException.class);
    }

    @Test
    void testEmptyNewPath() {
        Config config = new Config();
        config.setOldPath("");
        ConfigValidator configValidator = new ConfigValidator();

        Exception exception = Assertions.catchException(() -> configValidator.validate(config));

        Assertions.assertThat(exception).isInstanceOf(JAsyncApiCmpUserException.class);
    }

    @Test
    void successful() {
        Config config = new Config();
        config.setNewPath("/path");
        config.setOldPath("/path");
        ConfigValidator configValidator = new ConfigValidator();

        Exception exception = Assertions.catchException(() -> configValidator.validate(config));

        Assertions.assertThat(exception).isNull();
    }
}