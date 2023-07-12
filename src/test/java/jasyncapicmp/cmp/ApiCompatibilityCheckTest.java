package jasyncapicmp.cmp;

import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.model.AsyncApi;
import jasyncapicmp.parser.AsyncApiParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class ApiCompatibilityCheckTest {

	@Test
	void testNewChannel() {
		AsyncApiComparator comparator = new AsyncApiComparator();
		AsyncApiParser parser = new AsyncApiParser();
		AsyncApi oldApi = parser.parse(("""
channels:
                      """).getBytes(StandardCharsets.UTF_8), "/path");
		AsyncApi newApi = parser.parse(("""
channels:
  userSignedUp:
    subscribe:
      operationId: userSignup
      summary: Action to sign a user up.
      description: A longer description
      message:
        contentType: text/plain
                      """).getBytes(StandardCharsets.UTF_8), "/path");

		ObjectDiff objectDiff = comparator.compare(oldApi, newApi);
		ApiCompatibilityCheck apiCompatibilityCheck = new ApiCompatibilityCheck();
		apiCompatibilityCheck.check(objectDiff);

		Assertions.assertThat(objectDiff.getMapDiffs().get("channels")
				.getMapDiffEntries().get("userSignedUp")
				.getApiCompatibilityChanges()).contains(ApiCompatibilityChange.CHANNEL_ADDED);
	}

	@Test
	void testChannelItemMessageContentTypeChanged() {
		AsyncApiComparator comparator = new AsyncApiComparator();
		AsyncApiParser parser = new AsyncApiParser();
		AsyncApi oldApi = parser.parse(("""
channels:
  userSignedUp:
    subscribe:
      operationId: userSignup
      summary: Action to sign a user up.
      description: A longer description
      message:
        contentType: application/json
                      """).getBytes(StandardCharsets.UTF_8), "/path");
		AsyncApi newApi = parser.parse(("""
channels:
  userSignedUp:
    subscribe:
      operationId: userSignup
      summary: Action to sign a user up.
      description: A longer description
      message:
        contentType: text/plain
                      """).getBytes(StandardCharsets.UTF_8), "/path");

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
		AsyncApiComparator comparator = new AsyncApiComparator();
		AsyncApiParser parser = new AsyncApiParser();
		AsyncApi oldApi = parser.parse(("""
channels:
  userSignedUp:
    subscribe:
      operationId: userSignup
      summary: Action to sign a user up.
      description: A longer description
      message:
        schemaFormat: application/schema+json;version=draft-07
        payload:
            type: object
            properties:
                user:
                    type: string
                      """).getBytes(StandardCharsets.UTF_8), "/path");
		AsyncApi newApi = parser.parse(("""
channels:
  userSignedUp:
    subscribe:
      operationId: userSignup
      summary: Action to sign a user up.
      description: A longer description
      message:
        schemaFormat: application/schema+yaml;version=draft-07
        payload:
            type: object
            properties:
                user:
                    type: string
                      """).getBytes(StandardCharsets.UTF_8), "/path");

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
