package jasyncapicmp.util;

import jasyncapicmp.cmp.ApiComparator;
import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.model.asyncapi.AsyncApi;
import jasyncapicmp.model.openapi.OpenApi;
import jasyncapicmp.parser.ApiParser;

import java.nio.charset.StandardCharsets;

public class TestUtil {

    public static ObjectDiff compareAsyncApiYaml(String oldYaml, String newYaml) {
        ApiParser apiParser = new ApiParser();
        AsyncApi oldAsyncApi = (AsyncApi) apiParser.parse(oldYaml.getBytes(StandardCharsets.UTF_8), "/old");
        AsyncApi newAsyncApi = (AsyncApi) apiParser.parse(newYaml.getBytes(StandardCharsets.UTF_8), "/new");
        ApiComparator comparator = new ApiComparator();
        return comparator.compare(oldAsyncApi, newAsyncApi);
    }

	public static ObjectDiff compareOpenApiYaml(String oldYaml, String newYaml) {
		ApiParser apiParser = new ApiParser();
		OpenApi oldApi = (OpenApi) apiParser.parse(oldYaml.getBytes(StandardCharsets.UTF_8), "/old");
		OpenApi newApi = (OpenApi) apiParser.parse(newYaml.getBytes(StandardCharsets.UTF_8), "/new");
		ApiComparator comparator = new ApiComparator();
		return comparator.compare(oldApi, newApi);
	}
}
