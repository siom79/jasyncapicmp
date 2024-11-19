package jasyncapicmp.util;

import jasyncapicmp.cmp.ApiComparator;
import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.model.asyncapi.AsyncApi;
import jasyncapicmp.parser.ApiParser;

import java.nio.charset.StandardCharsets;

public class TestUtil {

    public static ObjectDiff compareYaml(String oldYaml, String newYaml) {
        ApiParser apiParser = new ApiParser();
        AsyncApi oldAsyncApi = (AsyncApi) apiParser.parse(oldYaml.getBytes(StandardCharsets.UTF_8), "/old");
        AsyncApi newAsyncApi = (AsyncApi) apiParser.parse(newYaml.getBytes(StandardCharsets.UTF_8), "new");
        ApiComparator comparator = new ApiComparator();
        return comparator.compare(oldAsyncApi, newAsyncApi);
    }
}
