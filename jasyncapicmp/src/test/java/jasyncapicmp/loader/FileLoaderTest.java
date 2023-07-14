package jasyncapicmp.loader;

import jasyncapicmp.JAsyncApiCmpUserException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FileLoaderTest {

    @Test
    public void testFileNotExists() {
        FileLoader fileLoader = new FileLoader();

        Exception exception = Assertions.catchException(() -> fileLoader.loadFileFromDisc("/not-exists"));

        Assertions.assertThat(exception).isInstanceOf(JAsyncApiCmpUserException.class);
    }
}