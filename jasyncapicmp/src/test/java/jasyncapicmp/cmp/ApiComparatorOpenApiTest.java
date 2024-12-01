package jasyncapicmp.cmp;

import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.model.openapi.OpenApi;
import jasyncapicmp.parser.ApiParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class ApiComparatorOpenApiTest {

	@Test
	void testPathAdded() {
		ApiComparator comparator = new ApiComparator();
		ApiParser parser = new ApiParser();
		OpenApi oldApi = (OpenApi) parser.parse(("openapi: 3.1.0\n" +
			"info:\n" +
			"  title: \"Test\"\n" +
			"  version: \"1.0.0\"\n" +
			"paths:\n" +
			"  /pets:\n" +
			"      get:\n" +
			"        operationId: getPets\n" +
			" ").getBytes(StandardCharsets.UTF_8), "/path");
		OpenApi newApi = (OpenApi) parser.parse(("openapi: 3.1.0\n" +
			"info:\n" +
			"  title: \"Test\"\n" +
			"  version: \"1.0.0\"\n" +
			"paths:\n" +
			"  /pets:\n" +
			"      get:\n" +
			"        operationId: getPets\n" +
			"  /food:\n" +
			"      get:\n" +
			"        operationId: getFood\n" +
			" ").getBytes(StandardCharsets.UTF_8), "/path");

		ObjectDiff objectDiff = comparator.compare(oldApi, newApi);

		Assertions.assertThat(objectDiff.getMapDiffs().get("paths").getMapDiffEntries().get("/pets").getChangeStatus()).isEqualTo(ChangeStatus.UNCHANGED);
		Assertions.assertThat(objectDiff.getMapDiffs().get("paths").getMapDiffEntries().get("/food").getChangeStatus()).isEqualTo(ChangeStatus.ADDED);
		Assertions.assertThat(objectDiff.getMapDiffs().get("paths").getMapDiffEntries().get("/food").getObjectDiff().getChangeStatus()).isEqualTo(ChangeStatus.ADDED);
	}
}
