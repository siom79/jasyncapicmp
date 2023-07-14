package jasyncapicmp.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilsTest {

    @Test
    void testIsEmptyNull() {
        boolean result = StringUtils.isEmpty(null);
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void testIsEmptyEmptyString() {
        boolean result = StringUtils.isEmpty("");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void testIsNotEmptyEmptyString() {
        boolean result = StringUtils.isEmpty("foo");
        Assertions.assertThat(result).isFalse();
    }
}