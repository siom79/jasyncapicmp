package jasyncapicmp.cmp;

import jasyncapicmp.cmp.compat.ChannelDiff;
import jasyncapicmp.cmp.diff.ObjectDiff;

public class ApiCompatibilityCheck {

	public ObjectDiff check(ObjectDiff objectDiff) {
		ChannelDiff.check(objectDiff);
		return objectDiff;
	}
}
