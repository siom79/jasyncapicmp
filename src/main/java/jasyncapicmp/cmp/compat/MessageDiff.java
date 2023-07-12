package jasyncapicmp.cmp.compat;

import jasyncapicmp.cmp.ApiCompatibilityChange;
import jasyncapicmp.cmp.ChangeStatus;
import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.cmp.diff.StringDiff;

public class MessageDiff {

	public static void check(ObjectDiff messageDiff) {
		StringDiff messageIdDiff = messageDiff.getStringDiffs().get("messageId");
		if (messageIdDiff != null) {
			if (messageIdDiff.getChangeStatus() == ChangeStatus.CHANGED) {
				messageIdDiff.addCompatibilityChange(ApiCompatibilityChange.MESSAGE_MESSAGE_ID_CHANGED);
			}
		}
		StringDiff contentTypeDiff = messageDiff.getStringDiffs().get("contentType");
		if (contentTypeDiff != null) {
			if (contentTypeDiff.getChangeStatus() == ChangeStatus.CHANGED) {
				contentTypeDiff.addCompatibilityChange(ApiCompatibilityChange.MESSAGE_CONTENT_TYPE_CHANGED);
			}
		}
		StringDiff schemaFormatDiff = messageDiff.getStringDiffs().get("schemaFormat");
		if (schemaFormatDiff != null) {
			if (schemaFormatDiff.getChangeStatus() == ChangeStatus.CHANGED) {
				schemaFormatDiff.addCompatibilityChange(ApiCompatibilityChange.MESSAGE_SCHEMA_FORMAT_CHANGED);
			}
		}
		ObjectDiff payloadDiff = messageDiff.getObjectDiffs().get("payload");
		SchemaDiff.check(payloadDiff);
	}
}
