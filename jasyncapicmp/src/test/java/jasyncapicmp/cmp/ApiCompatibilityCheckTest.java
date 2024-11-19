package jasyncapicmp.cmp;

import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.model.asyncapi.AsyncApi;
import jasyncapicmp.parser.ApiParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class ApiCompatibilityCheckTest {

	@Test
	void testNewChannel() {
		ApiComparator comparator = new ApiComparator();
		ApiParser parser = new ApiParser();
		AsyncApi oldApi = (AsyncApi) parser.parse(("asyncapi: 2.6.0\nchannels:").getBytes(StandardCharsets.UTF_8), "/path");
		AsyncApi newApi = (AsyncApi) parser.parse(("asyncapi: 2.6.0\n" +
			"channels:\n" +
				"  userSignedUp:\n" +
				"    subscribe:\n" +
				"      operationId: userSignup\n" +
				"      summary: Action to sign a user up.\n" +
				"      description: A longer description\n" +
				"      message:\n" +
				"        contentType: text/plain").getBytes(StandardCharsets.UTF_8), "/path");

		ObjectDiff objectDiff = comparator.compare(oldApi, newApi);
		ApiCompatibilityCheck apiCompatibilityCheck = new ApiCompatibilityCheck();
		apiCompatibilityCheck.check(objectDiff);

		Assertions.assertThat(objectDiff.getMapDiffs().get("channels")
				.getMapDiffEntries().get("userSignedUp")
				.getApiCompatibilityChanges()).contains(ApiCompatibilityChange.CHANNEL_ADDED);
	}

	@Test
	void testChannelItemMessageContentTypeChanged() {
		ApiComparator comparator = new ApiComparator();
		ApiParser parser = new ApiParser();
		AsyncApi oldApi = (AsyncApi) parser.parse(("asyncapi: 2.6.0\n" +
			    "channels:\n" +
				"  userSignedUp:\n" +
				"    subscribe:\n" +
				"      operationId: userSignup\n" +
				"      summary: Action to sign a user up.\n" +
				"      description: A longer description\n" +
				"      message:\n" +
				"        contentType: application/json").getBytes(StandardCharsets.UTF_8), "/path");
		AsyncApi newApi = (AsyncApi) parser.parse(("asyncapi: 2.6.0\n" +
			    "channels:\n" +
				"  userSignedUp:\n" +
				"    subscribe:\n" +
				"      operationId: userSignup\n" +
				"      summary: Action to sign a user up.\n" +
				"      description: A longer description\n" +
				"      message:\n" +
				"        contentType: text/plain").getBytes(StandardCharsets.UTF_8), "/path");

		ObjectDiff objectDiff = comparator.compare(oldApi, newApi);
		ApiCompatibilityCheck apiCompatibilityCheck = new ApiCompatibilityCheck();
		apiCompatibilityCheck.check(objectDiff);

		Assertions.assertThat(objectDiff.getMapDiffs().get("channels")
				.getMapDiffEntries().get("userSignedUp")
				.getObjectDiff().getObjectDiffs().get("subscribe")
				.getObjectDiffs().get("message")
				.getStringDiffs().get("contentType").getApiCompatibilityChanges()).contains(ApiCompatibilityChange.MESSAGE_CONTENT_TYPE_CHANGED);
	}

	@Test
	void testChannelItemMessageSchemaFormatChanged() {
		ApiComparator comparator = new ApiComparator();
		ApiParser parser = new ApiParser();
		AsyncApi oldApi = (AsyncApi) parser.parse(("asyncapi: 2.6.0\n" +
			    "channels:\n" +
				"  userSignedUp:\n" +
				"    subscribe:\n" +
				"      operationId: userSignup\n" +
				"      summary: Action to sign a user up.\n" +
				"      description: A longer description\n" +
				"      message:\n" +
				"        schemaFormat: application/schema+json;version=draft-07\n" +
				"        payload:\n" +
				"            type: object\n" +
				"            properties:\n" +
				"                user:\n" +
				"                    type: string").getBytes(StandardCharsets.UTF_8), "/path");
		AsyncApi newApi = (AsyncApi) parser.parse(("asyncapi: 2.6.0\n" +
			    "channels:\n" +
				"  userSignedUp:\n" +
				"    subscribe:\n" +
				"      operationId: userSignup\n" +
				"      summary: Action to sign a user up.\n" +
				"      description: A longer description\n" +
				"      message:\n" +
				"        schemaFormat: application/schema+yaml;version=draft-07\n" +
				"        payload:\n" +
				"            type: object\n" +
				"            properties:\n" +
				"                user:\n" +
				"                    type: string").getBytes(StandardCharsets.UTF_8), "/path");

		ObjectDiff objectDiff = comparator.compare(oldApi, newApi);
		ApiCompatibilityCheck apiCompatibilityCheck = new ApiCompatibilityCheck();
		apiCompatibilityCheck.check(objectDiff);

		Assertions.assertThat(objectDiff.getMapDiffs().get("channels")
				.getMapDiffEntries().get("userSignedUp")
				.getObjectDiff().getObjectDiffs().get("subscribe")
				.getObjectDiffs().get("message")
				.getStringDiffs().get("schemaFormat").getApiCompatibilityChanges()).contains(ApiCompatibilityChange.MESSAGE_SCHEMA_FORMAT_CHANGED);
	}
}
