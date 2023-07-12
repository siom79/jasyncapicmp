package jasyncapicmp.cmp.compat;

import jasyncapicmp.cmp.diff.ObjectDiff;

public class ChannelItemDiff {

	public static void check(ObjectDiff objectDiff) {
		ObjectDiff subscribe = objectDiff.getObjectDiffs().get("subscribe");
		if (subscribe != null) {
			OperationDiff.check(subscribe);
		}
		ObjectDiff publish = objectDiff.getObjectDiffs().get("publish");
		if (publish != null) {
			OperationDiff.check(publish);
		}
	}
}
