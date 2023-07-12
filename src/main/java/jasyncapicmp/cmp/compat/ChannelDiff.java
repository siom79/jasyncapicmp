package jasyncapicmp.cmp.compat;

import jasyncapicmp.cmp.ApiCompatibilityChange;
import jasyncapicmp.cmp.ChangeStatus;
import jasyncapicmp.cmp.diff.MapDiff;
import jasyncapicmp.cmp.diff.ObjectDiff;

import java.util.Map;

public class ChannelDiff {

	public static void check(ObjectDiff objectDiff) {
		MapDiff mapDiff = objectDiff.getMapDiffs().get("channels");
		if (mapDiff != null) {
			for (Map.Entry<String, MapDiff.MapDiffEntry> mapEntry : mapDiff.getMapDiffEntries().entrySet()) {
				MapDiff.MapDiffEntry mapDiffEntry = mapEntry.getValue();
				if (mapDiffEntry.getChangeStatus() == ChangeStatus.ADDED) {
					mapDiffEntry.addCompatibilityChange(ApiCompatibilityChange.CHANNEL_ADDED);
				} else if (mapDiffEntry.getChangeStatus() == ChangeStatus.REMOVED) {
					mapDiffEntry.addCompatibilityChange(ApiCompatibilityChange.CHANNEL_REMOVED);
				} else {
					ChannelItemDiff.check(mapDiffEntry.getObjectDiff());
				}
			}
		}
	}
}
