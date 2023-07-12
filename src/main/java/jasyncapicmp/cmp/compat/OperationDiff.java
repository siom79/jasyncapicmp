package jasyncapicmp.cmp.compat;

import jasyncapicmp.cmp.ApiCompatibilityChange;
import jasyncapicmp.cmp.ChangeStatus;
import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.cmp.diff.StringDiff;

public class OperationDiff {

	public static void check(ObjectDiff operationDiff) {
		StringDiff operationIdDiff = operationDiff.getStringDiffs().get("operationId");
		if (operationIdDiff != null) {
			if (operationIdDiff.getChangeStatus() == ChangeStatus.CHANGED) {
				operationIdDiff.addCompatibilityChange(ApiCompatibilityChange.OPERATION_OPERATION_ID_CHANGED);
			}
		}
		ObjectDiff messageDiff = operationDiff.getObjectDiffs().get("message");
		if (messageDiff != null) {
			MessageDiff.check(messageDiff);
		}
	}
}
