package jasyncapicmp.util;

import jasyncapicmp.cmp.diff.*;

public class AccessHelper {

	private AccessHelper() {
		// private constructor
	}

	public static DiffModel get(ObjectDiff objectDiffArg, String dottedStr) {
		ObjectDiff currentObjDiff = objectDiffArg;
		String[] parts = dottedStr.split("\\.");
		for (int i = 0; i < parts.length; i++) {
			String token = parts[i];
			StringDiff stringDiff = currentObjDiff.getStringDiffs().get(token);
			if (stringDiff != null) {
				if (i == parts.length - 1) {
					return stringDiff;
				} else {
					throw new IllegalArgumentException("StringDiff found at " + token);
				}
			}
			IntegerDiff integerDiff = currentObjDiff.getIntegerDiffs().get(token);
			if (integerDiff != null) {
				if (i == parts.length - 1) {
					return integerDiff;
				} else {
					throw new IllegalArgumentException("IntegerDiff found at " + token);
				}
			}
			MapDiff mapDiff = currentObjDiff.getMapDiffs().get(token);
			if (mapDiff != null) {
				if (i == parts.length - 1) {
					return mapDiff;
				} else {
					String nextToken = parts[i + 1];
					MapDiff.MapDiffEntry entry = mapDiff.getMapDiffEntries().get(nextToken);
					if (entry != null) {
						if (i + 1 == parts.length - 1) {
							return entry;
						} else {
							currentObjDiff = entry.getObjectDiff();
							i++;
							continue;
						}
					}
				}
			}
			ListDiff listDiff = currentObjDiff.getListDiffs().get(token);
			if (listDiff != null) {
				if (i == parts.length - 1) {
					return listDiff;
				} else {
					if (!listDiff.getListDiffsEntries().isEmpty()) {
						currentObjDiff = listDiff.getListDiffsEntries().get(0).getObjectDiff();
						continue;
					} else {
						throw new IllegalArgumentException("Empty list: " + token);
					}
				}
			}
			if (token.contains("[") && token.contains("]") && token.indexOf("[") < token.indexOf("]")) {
				String listName = token.substring(0, token.indexOf("["));
				String indexStr = token.substring(token.indexOf("[") + 1, token.indexOf("]"));
				int index = Integer.parseInt(indexStr);
				listDiff = currentObjDiff.getListDiffs().get(listName);
				if (listDiff != null) {
					ListDiff.ListDiffEntry<?> listDiffEntry = listDiff.getListDiffsEntries().get(index);
					if (i == parts.length - 1) {
						return listDiffEntry;
					} else {
						currentObjDiff = listDiffEntry.getObjectDiff();
						continue;
					}
				}
			}
			ObjectDiff objectDiffFound = currentObjDiff.getObjectDiffs().get(token);
			if (objectDiffFound != null) {
				if (i == parts.length - 1) {
					return objectDiffFound;
				} else {
					currentObjDiff = objectDiffFound;
					continue;
				}
			}
			throw new IllegalArgumentException("Not found: " + token);
		}
		throw new IllegalArgumentException("Not found: " + dottedStr);
	}
}
