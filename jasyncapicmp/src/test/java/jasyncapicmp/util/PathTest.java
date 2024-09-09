package jasyncapicmp.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PathTest {

	@Test
	void testSimplePath() {
		Path path = new Path();

		path.down("channels").down("my-channel");

		Assertions.assertThat(path.toString()).isEqualTo("channels.my-channel");
	}

	@Test
	void testPathWithUp() {
		Path path = new Path();

		path.down("channels").up().down("my-channel");

		Assertions.assertThat(path.toString()).isEqualTo("my-channel");
	}
}
