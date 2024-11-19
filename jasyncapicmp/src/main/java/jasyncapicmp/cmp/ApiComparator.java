package jasyncapicmp.cmp;

import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.model.asyncapi.AsyncApi;
import jasyncapicmp.model.openapi.OpenApi;

public class ApiComparator {

    public ObjectDiff compare(AsyncApi oldApi, AsyncApi newApi) {
        return ObjectDiff.compare(AsyncApi.class, oldApi, newApi);
    }

	public ObjectDiff compare(OpenApi oldApi, OpenApi newApi) {
		return ObjectDiff.compare(OpenApi.class, oldApi, newApi);
	}
}
