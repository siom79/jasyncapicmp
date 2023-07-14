package jasyncapicmp.cmp;

import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.model.AsyncApi;

public class AsyncApiComparator {

    public ObjectDiff compare(AsyncApi oldApi, AsyncApi newApi) {
        return ObjectDiff.compare(AsyncApi.class, oldApi, newApi);
    }
}
