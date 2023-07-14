package jasyncapicmp.cmp.compat;

import jasyncapicmp.cmp.diff.ObjectDiff;

public class ChannelItemCompatibilityCheck {

	public static void check(ObjectDiff objectDiff) {
		ObjectDiff subscribe = objectDiff.getObjectDiffs().get("subscribe");
		if (subscribe != null) {
			OperationCompatibilityCheck.check(subscribe);
		}
		ObjectDiff publish = objectDiff.getObjectDiffs().get("publish");
		if (publish != null) {
			OperationCompatibilityCheck.check(publish);
		}
	}
}
