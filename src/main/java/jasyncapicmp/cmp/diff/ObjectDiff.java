package jasyncapicmp.cmp.diff;

import jasyncapicmp.JAsyncApiCmpUserException;
import jasyncapicmp.cmp.ChangeStatus;
import jasyncapicmp.model.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class ObjectDiff {
    private Class<? extends Model> type;
    private Model oldValue;
    private Model newValue;
    private ChangeStatus changeStatus = ChangeStatus.UNCHANGED;

    private Map<String, StringDiff> stringDiffs = new HashMap<>();
    private Map<String, ObjectDiff> objectDiffs = new HashMap<>();
    private Map<String, MapDiff> mapDiffs = new HashMap<>();
    private Map<String, ListDiff> listDiffs = new HashMap<>();

    public ObjectDiff(Class<? extends Model> type) {
        this.type = type;
    }

    public static ObjectDiff compare(Class<? extends Model> objType, Object oldObj, Object newObj) {
        try {
            ObjectDiff objectDiff = new ObjectDiff(objType);
            objectDiff.setOldValue((Model) oldObj);
            objectDiff.setNewValue((Model) newObj);
            if (oldObj == null && newObj == null) {
                objectDiff.setChangeStatus(ChangeStatus.UNCHANGED);
            } else if (oldObj == null) {
                objectDiff.setChangeStatus(ChangeStatus.ADDED);
            } else if (newObj == null) {
                objectDiff.setChangeStatus(ChangeStatus.REMOVED);
            } else {
                Field[] declaredFields = objType.getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    Class<?> fieldType = field.getType();
                    String fieldName = field.getName();
                    if (String.class.isAssignableFrom(fieldType)) {
                        compareString(oldObj, newObj, objectDiff, field, fieldName);
                    } else if (Model.class.isAssignableFrom(fieldType)) {
                        compareModel(oldObj, newObj, objectDiff, field, fieldType, fieldName);
                    } else if (Map.class.isAssignableFrom(fieldType)) {
                        compareMaps(oldObj, newObj, objectDiff, field, fieldName);
                    } else if (List.class.isAssignableFrom(fieldType)) {
                        compareLists(oldObj, newObj, objectDiff, field, fieldName);
                    }
                }
            }
            return objectDiff;
        } catch (IllegalAccessException e) {
            throw new JAsyncApiCmpUserException("Failed to access field: " + e.getMessage(), e);
        }
    }

    private static void compareLists(Object oldObj, Object newObj, ObjectDiff objectDiff, Field field, String fieldName) throws IllegalAccessException {
        ListDiff listDiff = ListDiff.compare((List) field.get(oldObj), (List) field.get(newObj));
        objectDiff.listDiffs.put(fieldName, listDiff);
    }

    private static void compareMaps(Object oldObj, Object newObj, ObjectDiff objectDiff, Field field, String fieldName) throws IllegalAccessException {
        MapDiff mapDiff = MapDiff.compare((Map<String, Model>) field.get(oldObj), (Map<String, Model>) field.get(newObj));
        objectDiff.mapDiffs.put(fieldName, mapDiff);
    }

    private static void compareModel(Object oldObj, Object newObj, ObjectDiff objectDiff, Field field, Class<?> fieldType, String fieldName) throws IllegalAccessException {
        Object oldFieldObj = field.get(oldObj);
        Object newFieldObj = field.get(newObj);
        ObjectDiff objectDiffField = new ObjectDiff((Class<? extends Model>) fieldType);
        objectDiffField.setOldValue((Model) oldFieldObj);
        objectDiffField.setNewValue((Model) oldFieldObj);
        if (oldFieldObj == null && newFieldObj == null) {
            objectDiffField.setChangeStatus(ChangeStatus.UNCHANGED);
            objectDiff.objectDiffs.put(fieldName, objectDiffField);
        } else if (oldFieldObj == null) {
            objectDiffField.setChangeStatus(ChangeStatus.ADDED);
            objectDiff.objectDiffs.put(fieldName, objectDiffField);
        } else if (newFieldObj == null) {
            objectDiffField.setChangeStatus(ChangeStatus.REMOVED);
            objectDiff.objectDiffs.put(fieldName, objectDiffField);
        } else {
            ObjectDiff fieldDiff = ObjectDiff.compare((Class<Model>) fieldType, oldFieldObj, newFieldObj);
            //TODO comparison
            objectDiff.objectDiffs.put(fieldName, fieldDiff);
        }
    }

    private static void compareString(Object oldObj, Object newObj, ObjectDiff objectDiff, Field field, String fieldName) throws IllegalAccessException {
        String oldFieldValue = (String) field.get(oldObj);
        String newFieldValue = (String) field.get(newObj);
        StringDiff stringDiff = StringDiff.compare(oldFieldValue, newFieldValue);
        objectDiff.stringDiffs.put(fieldName, stringDiff);
    }

    public boolean isNull() {
        return oldValue == null && newValue == null;
    }
}
