package jasyncapicmp.cmp.diff;

import jasyncapicmp.cmp.ApiCompatibilityChange;
import jasyncapicmp.cmp.ChangeStatus;
import jasyncapicmp.cmp.compat.HasCompatibilityChanges;
import jasyncapicmp.model.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class StringDiff implements DiffModel, HasCompatibilityChanges {
	private Model oldModel;
	private Model newModel;
	private String oldValue;
	private String newValue;
	private ChangeStatus changeStatus = ChangeStatus.UNCHANGED;
	private List<ApiCompatibilityChange> apiCompatibilityChanges = new ArrayList<>();

	public static StringDiff compare(Model oldModel, Model newModel, String oldValue, String newValue) {
		StringDiff stringDiff = new StringDiff();
		stringDiff.setOldModel(oldModel);
		stringDiff.setNewModel(newModel);
		stringDiff.setOldValue(oldValue);
		stringDiff.setNewValue(newValue);
		if (oldValue == null && newValue == null) {
			stringDiff.setChangeStatus(ChangeStatus.UNCHANGED);
		} else if (oldValue == null) {
			stringDiff.setChangeStatus(ChangeStatus.ADDED);
		} else if (newValue == null) {
			stringDiff.setChangeStatus(ChangeStatus.REMOVED);
		} else {
			if (oldValue.equals(newValue)) {
				stringDiff.setChangeStatus(ChangeStatus.UNCHANGED);
			} else {
				stringDiff.setChangeStatus(ChangeStatus.CHANGED);
			}
		}
		return stringDiff;
	}

	public boolean isNull() {
		return oldValue == null && newValue == null;
	}

	public void addCompatibilityChange(ApiCompatibilityChange apiCompatibilityChange) {
		this.apiCompatibilityChanges.add(apiCompatibilityChange);
	}
}
