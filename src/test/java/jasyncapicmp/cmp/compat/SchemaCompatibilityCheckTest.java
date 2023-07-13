package jasyncapicmp.cmp.compat;

import jasyncapicmp.cmp.ApiCompatibilityChange;
import jasyncapicmp.cmp.ApiCompatibilityCheck;
import jasyncapicmp.cmp.AsyncApiComparator;
import jasyncapicmp.cmp.ChangeStatus;
import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.model.AsyncApi;
import jasyncapicmp.parser.AsyncApiParser;
import jasyncapicmp.util.AccessHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class SchemaCompatibilityCheckTest {

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
	void testSchemaPropertyAddedDeeper() {
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
                    type: object
                    properties:
                        name:
                            type: string
                        email:
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
                    type: object
                    properties:
                        firstName:
                            type: string
                        lastName:
                            type: string
                        email:
                            type: string
                      """).getBytes(StandardCharsets.UTF_8), "/path");

		ObjectDiff objectDiff = comparator.compare(oldApi, newApi);
		ApiCompatibilityCheck apiCompatibilityCheck = new ApiCompatibilityCheck();
		apiCompatibilityCheck.check(objectDiff);

		Assertions.assertThat(AccessHelper.get(objectDiff, "channels.userSignedUp.subscribe.message.payload.properties.user.properties.name")
				.getChangeStatus()).isEqualTo(ChangeStatus.REMOVED);
		Assertions.assertThat(((HasCompatibilityChanges)AccessHelper.get(objectDiff,
				"channels.userSignedUp.subscribe.message.payload.properties.user.properties.name"))
				.getApiCompatibilityChanges()).contains(ApiCompatibilityChange.SCHEMA_PROPERTY_REMOVED);
		Assertions.assertThat(AccessHelper.get(objectDiff, "channels.userSignedUp.subscribe.message.payload.properties.user.properties.firstName")
				.getChangeStatus()).isEqualTo(ChangeStatus.ADDED);
		Assertions.assertThat(((HasCompatibilityChanges)AccessHelper.get(objectDiff,
				"channels.userSignedUp.subscribe.message.payload.properties.user.properties.firstName"))
				.getApiCompatibilityChanges()).contains(ApiCompatibilityChange.SCHEMA_PROPERTY_ADDED);
		Assertions.assertThat(AccessHelper.get(objectDiff, "channels.userSignedUp.subscribe.message.payload.properties.user.properties.lastName")
				.getChangeStatus()).isEqualTo(ChangeStatus.ADDED);
		Assertions.assertThat(((HasCompatibilityChanges)AccessHelper.get(objectDiff,
				"channels.userSignedUp.subscribe.message.payload.properties.user.properties.lastName"))
				.getApiCompatibilityChanges()).contains(ApiCompatibilityChange.SCHEMA_PROPERTY_ADDED);
		Assertions.assertThat(AccessHelper.get(objectDiff, "channels.userSignedUp.subscribe.message.payload.properties.user.properties.email")
				.getChangeStatus()).isEqualTo(ChangeStatus.UNCHANGED);
		Assertions.assertThat(((HasCompatibilityChanges)AccessHelper.get(objectDiff,
				"channels.userSignedUp.subscribe.message.payload.properties.user.properties.email"))
				.getApiCompatibilityChanges()).isEmpty();
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

		Assertions.assertThat(((HasCompatibilityChanges)AccessHelper.get(objectDiff,
				"channels.userSignedUp.subscribe.message.payload.properties.user.minLength"))
				.getApiCompatibilityChanges()).contains(ApiCompatibilityChange.SCHEMA_MIN_LENGTH_DECREASED);
	}

	@Test
	void testSchemaOneOfRef() {
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
        oneOf:
          - $ref: "#/components/messages/M1"
          - $ref: "#/components/messages/M2"
components:
  messages:
    M1:
      title: "Title1"
    M2:
      title: "Title2"
""").getBytes(StandardCharsets.UTF_8), "/path");
		AsyncApi newApi = parser.parse(("""
channels:
  userSignedUp:
    subscribe:
      operationId: userSignup
      summary: Action to sign a user up.
      description: A longer description
      message:
        oneOf:
          - $ref: "#/components/messages/M1"
          - $ref: "#/components/messages/M2"
components:
  messages:
    M1:
      title: "Title1"
    M2:
      title: "Title2"
                      """).getBytes(StandardCharsets.UTF_8), "/path");

		ObjectDiff objectDiff = comparator.compare(oldApi, newApi);
		ApiCompatibilityCheck apiCompatibilityCheck = new ApiCompatibilityCheck();
		apiCompatibilityCheck.check(objectDiff);

		Assertions.assertThat(AccessHelper.get(objectDiff,
				"channels.userSignedUp.subscribe.message.oneOf").getChangeStatus()
				).isEqualTo(ChangeStatus.UNCHANGED);
		Assertions.assertThat(AccessHelper.get(objectDiff,
				"channels.userSignedUp.subscribe.message.oneOf[0]").getChangeStatus()
		).isEqualTo(ChangeStatus.UNCHANGED);
		Assertions.assertThat(AccessHelper.get(objectDiff,
				"channels.userSignedUp.subscribe.message.oneOf[1]").getChangeStatus()
		).isEqualTo(ChangeStatus.UNCHANGED);
	}
}
