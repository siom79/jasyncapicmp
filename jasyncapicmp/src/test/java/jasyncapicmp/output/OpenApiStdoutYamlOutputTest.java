package jasyncapicmp.output;

import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.util.TestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OpenApiStdoutYamlOutputTest {

	@Test
	public void newOperation() {
		StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
		OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
		ObjectDiff objectDiff = TestUtil.compareOpenApiYaml(
			"openapi: 3.1.0\n" +
			"info:\n" +
			"  title: \"Test\"\n" +
			"  version: \"1.0.0\"\n" +
			"paths:\n" +
			"  /pets:\n" +
			"      get:\n" +
			"        operationId: getPets\n" +
			" ",
			"openapi: 3.1.0\n" +
				"info:\n" +
				"  title: \"Test\"\n" +
				"  version: \"1.0.0\"\n" +
				"paths:\n" +
				"  /pets:\n" +
				"      get:\n" +
				"        operationId: getPets\n" +
				"      post:\n" +
				"        operationId: postPets\n"
		);

		outputProcessor.process(objectDiff);
		String output = stdoutOutputTracker.toString();

		Assertions.assertThat(output).isEqualTo("openapi: 3.1.0 # ===\n" +
			"info: # ===\n" +
			"  title: Test # ===\n" +
			"  version: 1.0.0 # ===\n" +
			"paths: # ===\n" +
			"  /pets: # ===\n" +
			"    post: # +++\n" +
			"      operationId: postPets # +++\n" +
			"    get: # ===\n" +
			"      operationId: getPets # ===\n");
	}

	@Test
	public void removedOperation() {
		StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
		OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
		ObjectDiff objectDiff = TestUtil.compareOpenApiYaml(
			"openapi: 3.1.0\n" +
				"info:\n" +
				"  title: \"Test\"\n" +
				"  version: \"1.0.0\"\n" +
				"paths:\n" +
				"  /pets:\n" +
				"      get:\n" +
				"        operationId: getPets\n" +
				"      post:\n" +
				"        operationId: postPets\n",
			"openapi: 3.1.0\n" +
				"info:\n" +
				"  title: \"Test\"\n" +
				"  version: \"1.0.0\"\n" +
				"paths:\n" +
				"  /pets:\n" +
				"      get:\n" +
				"        operationId: getPets\n" +
				" "
		);

		outputProcessor.process(objectDiff);
		String output = stdoutOutputTracker.toString();

		Assertions.assertThat(output).isEqualTo("openapi: 3.1.0 # ===\n" +
			"info: # ===\n" +
			"  title: Test # ===\n" +
			"  version: 1.0.0 # ===\n" +
			"paths: # ===\n" +
			"  /pets: # ===\n" +
			"    post: # ---\n" +
			"      operationId: postPets # ---\n" +
			"    get: # ===\n" +
			"      operationId: getPets # ===\n");
	}

	@Test
	public void newPath() {
		StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
		OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
		ObjectDiff objectDiff = TestUtil.compareOpenApiYaml(
			"openapi: 3.1.0\n" +
				"info:\n" +
				"  title: \"Test\"\n" +
				"  version: \"1.0.0\"\n" +
				"paths:\n" +
				"  /pets:\n" +
				"    get:\n" +
				"      operationId: getPets\n",
			"openapi: 3.1.0\n" +
				"info:\n" +
				"  title: \"Test\"\n" +
				"  version: \"1.0.0\"\n" +
				"paths:\n" +
				"  /pets:\n" +
				"    get:\n" +
				"      operationId: getPets\n" +
				"  /food:\n" +
				"    get:\n" +
				"      operationId: postPets\n"
		);

		outputProcessor.process(objectDiff);
		String output = stdoutOutputTracker.toString();

		Assertions.assertThat(output).isEqualTo("openapi: 3.1.0 # ===\n" +
			"info: # ===\n" +
			"  title: Test # ===\n" +
			"  version: 1.0.0 # ===\n" +
			"paths: # ===\n" +
			"  /pets: # ===\n" +
			"    get: # ===\n" +
			"      operationId: getPets # ===\n" +
			"  /food: # +++\n" +
			"      operationId: postPets # +++\n");
	}

	@Test
	public void removedPath() {
		StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
		OutputProcessor outputProcessor = new OutputProcessor(stdoutOutputTracker);
		ObjectDiff objectDiff = TestUtil.compareOpenApiYaml(
			"openapi: 3.1.0\n" +
				"info:\n" +
				"  title: \"Test\"\n" +
				"  version: \"1.0.0\"\n" +
				"paths:\n" +
				"  /pets:\n" +
				"    get:\n" +
				"      operationId: getPets\n" +
				"  /food:\n" +
				"    get:\n" +
				"      operationId: postPets\n",
			"openapi: 3.1.0\n" +
				"info:\n" +
				"  title: \"Test\"\n" +
				"  version: \"1.0.0\"\n" +
				"paths:\n" +
				"  /pets:\n" +
				"    get:\n" +
				"      operationId: getPets\n"
		);

		outputProcessor.process(objectDiff);
		String output = stdoutOutputTracker.toString();

		Assertions.assertThat(output).isEqualTo("openapi: 3.1.0 # ===\n" +
			"info: # ===\n" +
			"  title: Test # ===\n" +
			"  version: 1.0.0 # ===\n" +
			"paths: # ===\n" +
			"  /pets: # ===\n" +
			"    get: # ===\n" +
			"      operationId: getPets # ===\n" +
			"  /food: # ---\n" +
			"      operationId: postPets # ---\n");
	}
}
