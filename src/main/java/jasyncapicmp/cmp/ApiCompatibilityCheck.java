package jasyncapicmp.cmp;

import jasyncapicmp.cmp.compat.ChannelCompatibilityCheck;
import jasyncapicmp.cmp.diff.ObjectDiff;

public class ApiCompatibilityCheck {

	public ObjectDiff check(ObjectDiff objectDiff) {
		ChannelCompatibilityCheck.check(objectDiff);
		return objectDiff;
	}
}
