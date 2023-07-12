package jasyncapicmp.cmp.compat;

import jasyncapicmp.cmp.ApiCompatibilityChange;

import java.util.List;

public interface HasCompatibilityChanges {
	void addCompatibilityChange(ApiCompatibilityChange apiCompatibilityChange);

	List<ApiCompatibilityChange> getApiCompatibilityChanges();
}
