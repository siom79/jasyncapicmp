package jasyncapicmp.cmp.diff;

import jasyncapicmp.JAsyncApiCmpTechnicalException;
import jasyncapicmp.cmp.ApiCompatibilityChange;
import jasyncapicmp.cmp.ChangeStatus;
import jasyncapicmp.cmp.compat.HasCompatibilityChanges;
import jasyncapicmp.model.ListId;
import jasyncapicmp.model.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ListDiff {
    private List oldValue;
    private List newValue;
    private ChangeStatus changeStatus = ChangeStatus.UNCHANGED;
    private List<ListDiffEntry<?>> listDiffsEntries = new ArrayList<>();

    public enum ListDiffEntryType {
        STRING,
        MODEL
    }

    @Getter
    @Setter
    @ToString
    public static class ListDiffEntry<T> implements HasCompatibilityChanges {
        ListDiffEntryType type;
        private T oldValue;
        private T newValue;
        private ChangeStatus changeStatus = ChangeStatus.UNCHANGED;
        private ObjectDiff objectDiff;
		private List<ApiCompatibilityChange> apiCompatibilityChanges = new ArrayList<>();

		public void addCompatibilityChange(ApiCompatibilityChange apiCompatibilityChange) {
			this.apiCompatibilityChanges.add(apiCompatibilityChange);
		}
    }


    public static ListDiff compare(List oldValue, List newValue) {
        ListDiff listDiff = new ListDiff();
        listDiff.setOldValue(oldValue);
        listDiff.setNewValue(newValue);
        if (oldValue == null && newValue == null) {
            listDiff.setChangeStatus(ChangeStatus.UNCHANGED);
        } else if (oldValue == null) {
            listDiff.setChangeStatus(ChangeStatus.ADDED);
        } else if (newValue == null) {
            listDiff.setChangeStatus(ChangeStatus.REMOVED);
        } else {
            if (oldValue.isEmpty() && newValue.isEmpty()) {
                listDiff.setChangeStatus(ChangeStatus.UNCHANGED);
            } else if (oldValue.isEmpty()) {
                compareListOneEmpty(newValue, ChangeStatus.ADDED, listDiff);
                listDiff.setChangeStatus(ChangeStatus.ADDED);
            } else if (newValue.isEmpty()) {
                compareListOneEmpty(oldValue, ChangeStatus.REMOVED, listDiff);
                listDiff.setChangeStatus(ChangeStatus.REMOVED);
            } else {
                compareLists(oldValue, newValue, listDiff);
                listDiff.setChangeStatus(ChangeStatus.CHANGED);
            }
        }
        return listDiff;
    }

    private static void compareLists(List oldList, List newList, ListDiff listDiff) {
        Object v = oldList.get(0);
        if (v instanceof String) {
            compareListsString(oldList, newList, listDiff);
        } else if (v instanceof Model) {
            compareListsModel(oldList, newList, listDiff);
        }
    }

    private static void compareListsModel(List oldList, List newList, ListDiff listDiff) {
        try {
            Field idField = getIdField(oldList.get(0).getClass());
            if (idField != null) {
                List<Model> processedNew = new ArrayList<>();
                for (Object o : oldList) {
                    Object idFieldValueOld = idField.get(o);
                    if (idFieldValueOld != null) {
                        Object foundInNew = null;
                        for (Object n : newList) {
                            Object idFieldValueNew = idField.get(n);
                            if (idFieldValueNew != null) {
                                if (idFieldValueNew.equals(idFieldValueOld)) {
                                    foundInNew = n;
                                    break;
                                }
                            }
                        }
                        ListDiffEntry<Model> listDiffEntry = new ListDiffEntry<>();
                        listDiffEntry.setType(ListDiffEntryType.MODEL);
                        listDiffEntry.setOldValue((Model) o);
                        if (foundInNew != null) {
                            listDiffEntry.setNewValue((Model) o);
                            listDiffEntry.setObjectDiff(ObjectDiff.compare((Class<? extends Model>) o.getClass(), o, foundInNew));
                            processedNew.add((Model) foundInNew);
                        } else {
                            listDiffEntry.setObjectDiff(ObjectDiff.compare((Class<? extends Model>) o.getClass(), o, null));
                            listDiffEntry.setChangeStatus(ChangeStatus.REMOVED);
                        }
                        listDiff.listDiffsEntries.add(listDiffEntry);
                    } else {
                        ListDiffEntry<Model> listDiffEntry = new ListDiffEntry<>();
                        listDiffEntry.setType(ListDiffEntryType.MODEL);
                        listDiffEntry.setOldValue((Model) o);
                        listDiffEntry.setObjectDiff(ObjectDiff.compare((Class<? extends Model>) o.getClass(), o, null));
                        listDiffEntry.setChangeStatus(ChangeStatus.REMOVED);
                        listDiff.listDiffsEntries.add(listDiffEntry);
                    }
                }
                for (Object n : newList) {
                    if (processedNew.stream().anyMatch(pn -> {
                        try {
                            return idField.get(pn) != null && idField.get(pn).equals(idField.get(n));
                        } catch (IllegalAccessException e) {
                            throw new JAsyncApiCmpTechnicalException("Unable to access field: " + e.getMessage(), e);
                        }
                    })) {
                        continue;
                    }
                    ListDiffEntry<Model> listDiffEntry = new ListDiffEntry<>();
                    listDiffEntry.setType(ListDiffEntryType.MODEL);
                    listDiffEntry.setNewValue((Model) n);
                    listDiffEntry.setObjectDiff(ObjectDiff.compare((Class<? extends Model>) n.getClass(), null, n));
                    listDiffEntry.setChangeStatus(ChangeStatus.ADDED);
                    listDiff.listDiffsEntries.add(listDiffEntry);
                }
            }
        } catch (IllegalAccessException e) {
            throw new JAsyncApiCmpTechnicalException("Unable to access field: " + e.getMessage(), e);
        }
    }

    private static Field getIdField(Class<?> aClass) {
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            ListId annotation = field.getAnnotation(ListId.class);
            if (annotation != null) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    private static void compareListsString(List oldList, List newList, ListDiff listDiff) {
        List<String> processedNew = new ArrayList<>();
        for (Object o : oldList) {
            boolean foundInNew = false;
            for (Object n : newList) {
                if (n.equals(o)) {
                    foundInNew = true;
                    break;
                }
            }
            ListDiffEntry<String> listDiffEntry = new ListDiffEntry<>();
            listDiffEntry.setType(ListDiffEntryType.STRING);
            listDiffEntry.setOldValue((String) o);
            if (foundInNew) {
                listDiffEntry.setNewValue((String) o);
                listDiffEntry.setChangeStatus(ChangeStatus.UNCHANGED);
                processedNew.add((String) o);
            } else {
                listDiffEntry.setChangeStatus(ChangeStatus.REMOVED);
            }
            listDiff.listDiffsEntries.add(listDiffEntry);
        }
        for (Object n : newList) {
            if (processedNew.contains(n)) {
                continue;
            }
            ListDiffEntry<String> listDiffEntry = new ListDiffEntry<>();
            listDiffEntry.setType(ListDiffEntryType.STRING);
            listDiffEntry.setNewValue((String) n);
            listDiffEntry.setChangeStatus(ChangeStatus.ADDED);
            listDiff.listDiffsEntries.add(listDiffEntry);
        }
    }

    private static void compareListOneEmpty(List list, ChangeStatus changeStatus, ListDiff listDiff) {
        Object v = list.get(0);
        if (v instanceof String) {
            for (Object o : list) {
                ListDiffEntry<String> listDiffEntry = new ListDiffEntry<>();
                listDiffEntry.setType(ListDiffEntryType.STRING);
                if (changeStatus == ChangeStatus.ADDED) {
                    listDiffEntry.setNewValue((String) o);
                } else if (changeStatus == ChangeStatus.REMOVED) {
                    listDiffEntry.setOldValue((String) o);
                }
                listDiffEntry.setChangeStatus(changeStatus);
                listDiff.listDiffsEntries.add(listDiffEntry);
            }
        } else if (v instanceof Model) {
            for (Object o : list) {
                ListDiffEntry<Model> listDiffEntry = new ListDiffEntry<>();
                listDiffEntry.setType(ListDiffEntryType.MODEL);
                if (changeStatus == ChangeStatus.ADDED) {
                    listDiffEntry.setNewValue((Model) o);
                    listDiffEntry.setObjectDiff(ObjectDiff.compare((Class<? extends Model>) o.getClass(), null, o));
                } else if (changeStatus == ChangeStatus.REMOVED) {
                    listDiffEntry.setOldValue((Model) o);
                    listDiffEntry.setObjectDiff(ObjectDiff.compare((Class<? extends Model>) o.getClass(), o, null));
                }
                listDiffEntry.setChangeStatus(changeStatus);
                listDiff.listDiffsEntries.add(listDiffEntry);
            }
        }
    }

    public boolean isEmpty() {
        return this.listDiffsEntries.isEmpty();
    }

	public ListDiffEntry<String> getListDiffEntryByName(String name) {
		for (ListDiffEntry<?> listDiffEntry : this.listDiffsEntries) {
			if (listDiffEntry.getType() == ListDiffEntryType.STRING) {
				ListDiffEntry<String> listDiffEntryStr = (ListDiffEntry<String>) listDiffEntry;
				if (name.equals(listDiffEntryStr.newValue) || name.equals(listDiffEntryStr.oldValue)) {
					return listDiffEntryStr;
				}
			}
		}
		return null;
	}
}
