package jasyncapicmp;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JAsyncApiCmpTechnicalExceptionTest {

    @Test
    void testException() {
        JAsyncApiCmpTechnicalException jAsyncApiCmpTechnicalException = new JAsyncApiCmpTechnicalException("msg");

        Assertions.assertThat(jAsyncApiCmpTechnicalException.getMessage()).isEqualTo("msg");
    }
}