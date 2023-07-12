package jasyncapicmp.cmp.compat;

import jasyncapicmp.cmp.ApiCompatibilityChange;
import jasyncapicmp.cmp.ApiCompatibilityCheck;
import jasyncapicmp.cmp.AsyncApiComparator;
import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.model.AsyncApi;
import jasyncapicmp.parser.AsyncApiParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class SchemaDiffTest {

	@Test
	void testSchemaPropertyAdded() {
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
                role:
                    type: string
                      """).getBytes(StandardCharsets.UTF_8), "/path");

		ObjectDiff objectDiff = comparator.compare(oldApi, newApi);
		ApiCompatibilityCheck apiCompatibilityCheck = new ApiCompatibilityCheck();
		apiCompatibilityCheck.check(objectDiff);

		Assertions.assertThat(objectDiff.getMapDiffs().get("channels")
				.getMapDiffEntries().get("userSignedUp")
				.getObjectDiff().getObjectDiffs().get("subscribe")
				.getObjectDiffs().get("message")
				.getObjectDiffs().get("payload")
				.getMapDiffs().get("properties")
				.getMapDiffEntries().get("role")
				.getApiCompatibilityChanges()).contains(ApiCompatibilityChange.SCHEMA_PROPERTY_ADDED);
	}

	@Test
	void testSchemaPropertyRemoved() {
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
                role:
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
				.getObjectDiffs().get("payload")
				.getMapDiffs().get("properties")
				.getMapDiffEntries().get("role")
				.getApiCompatibilityChanges()).contains(ApiCompatibilityChange.SCHEMA_PROPERTY_REMOVED);
	}

	@Test
	void testSchemaPropertyRemovedRequired() {
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
                role:
                    type: string
            required:
                - user
                - role
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
            required:
                - user
                      """).getBytes(StandardCharsets.UTF_8), "/path");

		ObjectDiff objectDiff = comparator.compare(oldApi, newApi);
		ApiCompatibilityCheck apiCompatibilityCheck = new ApiCompatibilityCheck();
		apiCompatibilityCheck.check(objectDiff);

		Assertions.assertThat(objectDiff.getMapDiffs().get("channels")
				.getMapDiffEntries().get("userSignedUp")
				.getObjectDiff().getObjectDiffs().get("subscribe")
				.getObjectDiffs().get("message")
				.getObjectDiffs().get("payload")
				.getListDiffs().get("required")
				.getListDiffEntryByName("role")
				.getApiCompatibilityChanges()).contains(ApiCompatibilityChange.SCHEMA_PROPERTY_REQUIRED_REMOVED);
	}

	@Test
	void testSchemaPropertyAddedRequired() {
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
            required:
                - user
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
                    minLength: 6
                role:
                    type: string
            required:
                - user
                - role
                      """).getBytes(StandardCharsets.UTF_8), "/path");

		ObjectDiff objectDiff = comparator.compare(oldApi, newApi);
		ApiCompatibilityCheck apiCompatibilityCheck = new ApiCompatibilityCheck();
		apiCompatibilityCheck.check(objectDiff);

		Assertions.assertThat(objectDiff.getMapDiffs().get("channels")
				.getMapDiffEntries().get("userSignedUp")
				.getObjectDiff().getObjectDiffs().get("subscribe")
				.getObjectDiffs().get("message")
				.getObjectDiffs().get("payload")
				.getListDiffs().get("required")
				.getListDiffEntryByName("role")
				.getApiCompatibilityChanges()).contains(ApiCompatibilityChange.SCHEMA_PROPERTY_REQUIRED_ADDED);
	}

	@Test
	void testSchemaMinLengthDecreased() {
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
                    minLength: 10
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
                    minLength: 5
                      """).getBytes(StandardCharsets.UTF_8), "/path");

		ObjectDiff objectDiff = comparator.compare(oldApi, newApi);
		ApiCompatibilityCheck apiCompatibilityCheck = new ApiCompatibilityCheck();
		apiCompatibilityCheck.check(objectDiff);

		Assertions.assertThat(objectDiff.getMapDiffs().get("channels")
				.getMapDiffEntries().get("userSignedUp")
				.getObjectDiff().getObjectDiffs().get("subscribe")
				.getObjectDiffs().get("message")
				.getObjectDiffs().get("payload")
				.getMapDiffs().get("properties")
				.getMapDiffEntries().get("user")
				.getObjectDiff().getIntegerDiffs().get("minLength")
				.getApiCompatibilityChanges()).contains(ApiCompatibilityChange.SCHEMA_MIN_LENGTH_DECREASED);
	}
}
