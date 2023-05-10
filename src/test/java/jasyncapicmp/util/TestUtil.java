package jasyncapicmp.util;

import jasyncapicmp.cmp.AsyncApiComparator;
import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.model.AsyncApi;
import jasyncapicmp.parser.AsyncApiParser;

import java.nio.charset.StandardCharsets;

public class TestUtil {

    public static ObjectDiff compareYaml(String oldYaml, String newYaml) {
        AsyncApiParser asyncApiParser = new AsyncApiParser();
        AsyncApi oldAsyncApi = asyncApiParser.parse(oldYaml.getBytes(StandardCharsets.UTF_8), "/old");
        AsyncApi newAsyncApi = asyncApiParser.parse(newYaml.getBytes(StandardCharsets.UTF_8), "new");
        AsyncApiComparator comparator = new AsyncApiComparator();
        return comparator.compare(oldAsyncApi, newAsyncApi);
    }
}
