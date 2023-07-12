package jasyncapicmp.cmp.diff;

import jasyncapicmp.cmp.ApiCompatibilityChange;
import jasyncapicmp.cmp.ChangeStatus;
import jasyncapicmp.cmp.compat.HasCompatibilityChanges;
import jasyncapicmp.model.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class MapDiff {
    private Map<String, MapDiffEntry> mapDiffEntries = new HashMap<>();
    private Map<String, Model> oldValue;
    private Map<String, Model> newValue;
    private ChangeStatus changeStatus = ChangeStatus.UNCHANGED;

    @Getter
    @Setter
    @ToString
    public static class MapDiffEntry implements HasCompatibilityChanges {
        private Model oldValue;
        private Model newValue;
        private ObjectDiff objectDiff;
        private ChangeStatus changeStatus = ChangeStatus.UNCHANGED;
		private List<ApiCompatibilityChange> apiCompatibilityChanges = new ArrayList<>();

		public void addCompatibilityChange(ApiCompatibilityChange apiCompatibilityChange) {
			this.apiCompatibilityChanges.add(apiCompatibilityChange);
		}
	}

    public static MapDiff compare(Map<String, Model> oldValue, Map<String, Model> newValue) {
        MapDiff mapDiff = new MapDiff();
        mapDiff.setOldValue(oldValue);
        mapDiff.setNewValue(newValue);
        if (oldValue == null && newValue == null) {
            mapDiff.setChangeStatus(ChangeStatus.UNCHANGED);
        } else if (oldValue == null) {
			for (Map.Entry<String, Model> newEntry : newValue.entrySet()) {
				String newEntryKey = newEntry.getKey();
				Model newEntryValue = newEntry.getValue();
				if (newEntryValue == null) {
					continue;
				}
				MapDiffEntry mapDiffEntry = new MapDiffEntry();
				mapDiffEntry.setNewValue(newEntryValue);
				mapDiffEntry.setObjectDiff(ObjectDiff.compare(newEntryValue.getClass(), null, newEntryValue));
				mapDiffEntry.setChangeStatus(ChangeStatus.ADDED);
				mapDiff.mapDiffEntries.put(newEntryKey, mapDiffEntry);
			}
            mapDiff.setChangeStatus(ChangeStatus.ADDED);
        } else if (newValue == null) {
			for (Map.Entry<String, Model> oldEntry : oldValue.entrySet()) {
				String oldEntryKey = oldEntry.getKey();
				Model oldEntryValue = oldEntry.getValue();
				if (oldEntryValue == null) {
					continue;
				}
				MapDiffEntry mapDiffEntry = new MapDiffEntry();
				mapDiffEntry.setOldValue(oldEntryValue);
				mapDiffEntry.setObjectDiff(ObjectDiff.compare(oldEntryValue.getClass(), oldEntryValue, null));
				mapDiffEntry.setChangeStatus(ChangeStatus.REMOVED);
				mapDiff.mapDiffEntries.put(oldEntryKey, mapDiffEntry);
			}
            mapDiff.setChangeStatus(ChangeStatus.REMOVED);
        } else {
            List<String> newProcessed = new ArrayList<>();
            for (Map.Entry<String, Model> oldEntry : oldValue.entrySet()) {
                String oldEntryKey = oldEntry.getKey();
                Model oldEntryValue = oldEntry.getValue();
                if (oldEntryValue == null) {
                    continue;
                }
                Model newMapEntry = newValue.get(oldEntryKey);
                MapDiffEntry mapDiffEntry = new MapDiffEntry();
                mapDiffEntry.setOldValue(oldEntryValue);
                if (newMapEntry == null) {
                    mapDiffEntry.setObjectDiff(ObjectDiff.compare(oldEntryValue.getClass(), oldEntryValue, null));
                    mapDiffEntry.setChangeStatus(ChangeStatus.REMOVED);
                } else {
                    mapDiffEntry.setNewValue(newMapEntry);
                    mapDiffEntry.setObjectDiff(ObjectDiff.compare(oldEntryValue.getClass(), oldEntryValue, newMapEntry));
                    mapDiffEntry.setChangeStatus(ChangeStatus.UNCHANGED);
                    newProcessed.add(oldEntryKey);
                }
                mapDiff.mapDiffEntries.put(oldEntryKey, mapDiffEntry);
            }
            for (Map.Entry<String, Model> newEntry : newValue.entrySet()) {
                String newEntryKey = newEntry.getKey();
                Model newEntryValue = newEntry.getValue();
                if (newEntryValue == null) {
                    continue;
                }
                if (!newProcessed.contains(newEntryKey)) {
                    MapDiffEntry mapDiffEntry = new MapDiffEntry();
                    mapDiffEntry.setNewValue(newEntryValue);
                    mapDiffEntry.setObjectDiff(ObjectDiff.compare(newEntryValue.getClass(), null, newEntryValue));
                    mapDiffEntry.setChangeStatus(ChangeStatus.ADDED);
                    mapDiff.mapDiffEntries.put(newEntryKey, mapDiffEntry);
                }
            }
        }
        return mapDiff;
    }

    public boolean isEmpty() {
        return this.mapDiffEntries.isEmpty();
    }
}
