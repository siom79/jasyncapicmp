package jasyncapicmp.cmp;

import jasyncapicmp.cmp.diff.MapDiff;
import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.cmp.diff.StringDiff;

import java.util.Map;

public class ApiCompatibilityCheck {

	public ObjectDiff check(ObjectDiff objectDiff) {
		checkChannels(objectDiff);
		return objectDiff;
	}

	private void checkChannels(ObjectDiff objectDiff) {
		MapDiff mapDiff = objectDiff.getMapDiffs().get("channels");
		if (mapDiff != null) {
			for (Map.Entry<String, MapDiff.MapDiffEntry> mapEntry : mapDiff.getMapDiffEntries().entrySet()) {
				MapDiff.MapDiffEntry mapDiffEntry = mapEntry.getValue();
				if (mapDiffEntry.getChangeStatus() == ChangeStatus.ADDED) {
					mapDiffEntry.addCompatibilityChange(ApiCompatibilityChange.CHANNEL_ADDED);
				} else if (mapDiffEntry.getChangeStatus() == ChangeStatus.REMOVED) {
					mapDiffEntry.addCompatibilityChange(ApiCompatibilityChange.CHANNEL_REMOVED);
				} else {
					checkChannelOperations(mapDiffEntry.getObjectDiff());
				}
			}
		}
	}

	private void checkChannelOperations(ObjectDiff objectDiff) {
		ObjectDiff subscribe = objectDiff.getObjectDiffs().get("subscribe");
		if (subscribe != null) {
			StringDiff operationIdDiff = subscribe.getStringDiffs().get("operationId");
			if (operationIdDiff != null) {
				if (operationIdDiff.getChangeStatus() == ChangeStatus.CHANGED) {
					operationIdDiff.addCompatibilityChange(ApiCompatibilityChange.CHANNEL_ITEM_SUBSCRIBE_OPERATION_ID_CHANGED);
				}
			}
			ObjectDiff messageDiff = subscribe.getObjectDiffs().get("message");
			if (messageDiff != null) {
				StringDiff contentTypeDiff = messageDiff.getStringDiffs().get("contentType");
				if (contentTypeDiff != null) {
					if (contentTypeDiff.getChangeStatus() == ChangeStatus.CHANGED) {
						contentTypeDiff.addCompatibilityChange(ApiCompatibilityChange.CHANNEL_ITEM_SUBSCRIBE_MESSAGE_CONTENT_TYPE_CHANGED);
					}
				}
			}
		}
	}
}
