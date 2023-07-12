package jasyncapicmp.cmp.compat;

import jasyncapicmp.cmp.ApiCompatibilityChange;
import jasyncapicmp.cmp.ChangeStatus;
import jasyncapicmp.cmp.diff.*;

import java.util.Map;

public class SchemaDiff {

	public static void check(ObjectDiff schemaDiff) {
		checkType(schemaDiff);
		checkProperties(schemaDiff);
		checkRequired(schemaDiff);
		checkMinLength(schemaDiff);
		checkMaxLength(schemaDiff);
	}

	private static void checkRequired(ObjectDiff schemaDiff) {
		ListDiff requiredDiff = schemaDiff.getListDiffs().get("required");
		if (requiredDiff != null) {
			for (ListDiff.ListDiffEntry<?> listDiffEntry : requiredDiff.getListDiffsEntries()) {
				ChangeStatus changeStatus = listDiffEntry.getChangeStatus();
				switch (changeStatus) {
					case ADDED -> listDiffEntry.addCompatibilityChange(ApiCompatibilityChange.SCHEMA_PROPERTY_REQUIRED_ADDED);
					case REMOVED -> listDiffEntry.addCompatibilityChange(ApiCompatibilityChange.SCHEMA_PROPERTY_REQUIRED_REMOVED);
				}
			}
		}
	}

	private static void checkProperties(ObjectDiff schemaDiff) {
		MapDiff propertiesDiff = schemaDiff.getMapDiffs().get("properties");
		if (propertiesDiff != null) {
			for (Map.Entry<String, MapDiff.MapDiffEntry> mapEntry : propertiesDiff.getMapDiffEntries().entrySet()) {
				MapDiff.MapDiffEntry mapDiffEntry = mapEntry.getValue();
				if (mapDiffEntry.getChangeStatus() == ChangeStatus.ADDED) {
					mapDiffEntry.addCompatibilityChange(ApiCompatibilityChange.SCHEMA_PROPERTY_ADDED);
				} else if (mapDiffEntry.getChangeStatus() == ChangeStatus.REMOVED) {
					mapDiffEntry.addCompatibilityChange(ApiCompatibilityChange.SCHEMA_PROPERTY_REMOVED);
				} else {
					check(mapDiffEntry.getObjectDiff());
				}
			}
		}
	}

	private static void checkType(ObjectDiff schemaDiff) {
		StringDiff typeDiff = schemaDiff.getStringDiffs().get("type");
		if (typeDiff != null) {
			if (typeDiff.getChangeStatus() == ChangeStatus.CHANGED) {
				typeDiff.addCompatibilityChange(ApiCompatibilityChange.SCHEMA_TYPE_CHANGED);
			}
		}
	}

	private static void checkMinLength(ObjectDiff schemaDiff) {
		IntegerDiff minLengthDiff = schemaDiff.getIntegerDiffs().get("minLength");
		if (minLengthDiff != null) {
			if (minLengthDiff.getChangeStatus() == ChangeStatus.CHANGED) {
				Integer newValue = minLengthDiff.getNewValue();
				Integer oldValue = minLengthDiff.getOldValue();
				if (newValue != null && oldValue != null) {
					int comparisonResult = newValue.compareTo(oldValue);
					if (comparisonResult > 0) {
						minLengthDiff.addCompatibilityChange(ApiCompatibilityChange.SCHEMA_MIN_LENGTH_INCREASED);
					} else if (comparisonResult < 0) {
						minLengthDiff.addCompatibilityChange(ApiCompatibilityChange.SCHEMA_MIN_LENGTH_DECREASED);
					}
				}
			}
		}
	}

	private static void checkMaxLength(ObjectDiff schemaDiff) {
		IntegerDiff maxLengthDiff = schemaDiff.getIntegerDiffs().get("maxLength");
		if (maxLengthDiff != null) {
			if (maxLengthDiff.getChangeStatus() == ChangeStatus.CHANGED) {
				Integer newValue = maxLengthDiff.getNewValue();
				Integer oldValue = maxLengthDiff.getOldValue();
				if (newValue != null && oldValue != null) {
					int comparisonResult = newValue.compareTo(oldValue);
					if (comparisonResult > 0) {
						maxLengthDiff.addCompatibilityChange(ApiCompatibilityChange.SCHEMA_MAX_LENGTH_INCREASED);
					} else if (comparisonResult < 0) {
						maxLengthDiff.addCompatibilityChange(ApiCompatibilityChange.SCHEMA_MAX_LENGTH_DECREASED);
					}
				}
			}
		}
	}
}
