package jasyncapicmp;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

class MainTest {

    @Test
    public void successful() {
        Exception exception = Assertions.catchException(() -> Main.main(new String[]{
                "-o", Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "old_3.0.0.yaml").toString(),
                "-n", Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "new_3.0.0.yaml").toString()
        }));

        Assertions.assertThat(exception).isNull();
    }

    @Test
    public void missingArgs() {
        Exception exception = Assertions.catchException(() -> Main.main(new String[]{}));

        Assertions.assertThat(exception).isNull();
    }

    @Test
    public void argsIsNull() {
        Exception exception = Assertions.catchException(() -> Main.main(null));

        Assertions.assertThat(exception).isNull();
    }
}