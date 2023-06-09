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
public class IntegerDiff implements DiffModel, HasCompatibilityChanges {
	private Model oldModel;
	private Model newModel;
	private Integer oldValue;
	private Integer newValue;
	private ChangeStatus changeStatus = ChangeStatus.UNCHANGED;
	private List<ApiCompatibilityChange> apiCompatibilityChanges = new ArrayList<>();

	public static IntegerDiff compare(Model oldModel, Model newModel, Integer oldValue, Integer newValue) {
		IntegerDiff integerDiff = new IntegerDiff();
		integerDiff.setOldModel(oldModel);
		integerDiff.setNewModel(newModel);
		integerDiff.setOldValue(oldValue);
		integerDiff.setNewValue(newValue);
		if (oldValue == null && newValue == null) {
			integerDiff.setChangeStatus(ChangeStatus.UNCHANGED);
		} else if (oldValue == null) {
			integerDiff.setChangeStatus(ChangeStatus.ADDED);
		} else if (newValue == null) {
			integerDiff.setChangeStatus(ChangeStatus.REMOVED);
		} else {
			if (oldValue.equals(newValue)) {
				integerDiff.setChangeStatus(ChangeStatus.UNCHANGED);
			} else {
				integerDiff.setChangeStatus(ChangeStatus.CHANGED);
			}
		}
		return integerDiff;
	}

	public boolean isNull() {
		return oldValue == null && newValue == null;
	}

	public void addCompatibilityChange(ApiCompatibilityChange apiCompatibilityChange) {
		this.apiCompatibilityChanges.add(apiCompatibilityChange);
	}
}
