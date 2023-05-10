package jasyncapicmp.output;

import jasyncapicmp.JAsyncApiCmpTechnicalException;
import jasyncapicmp.cmp.ChangeStatus;
import jasyncapicmp.cmp.diff.ListDiff;
import jasyncapicmp.cmp.diff.MapDiff;
import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.cmp.diff.StringDiff;
import jasyncapicmp.model.AsyncApi;
import jasyncapicmp.model.Model;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class StdoutYamlOutput {

    @ToString
    private static class Indent {
        int indent;
        boolean nextTimeNoIndent;

        public Indent(int indent, boolean nextTimeNoIndent) {
            this.indent = indent;
            this.nextTimeNoIndent = nextTimeNoIndent;
        }

        public Indent incDefault() {
            return new Indent(indent + 2, nextTimeNoIndent);
        }

        public Indent incListIndent() {
            return new Indent(indent + 2, nextTimeNoIndent);
        }

        public Indent nextTimeNoIndent() {
            nextTimeNoIndent = true;
            return this;
        }
    }

    public String print(ObjectDiff objectDiff) {
        StringBuilder sb = new StringBuilder();
        printTopLevel(sb, objectDiff, new Indent(0, false));
        System.out.println(sb);
        return sb.toString();
    }

    private void printTopLevel(StringBuilder sb, ObjectDiff objectDiff, Indent indent) {
        Field[] declaredFields = AsyncApi.class.getDeclaredFields();
        for (Field field : declaredFields) {
            Class<?> type = field.getType();
            String name = field.getName();
            if (type == String.class) {
                StringDiff stringDiff = objectDiff.getStringDiffs().get(name);
                if (stringDiff != null) {
                    printStringDiff(sb, indent, name, stringDiff);
                }
            } else if (type == Map.class) {
                MapDiff mapDiff = objectDiff.getMapDiffs().get(name);
                if (mapDiff != null) {
                    printMapDiff(sb, indent, name, mapDiff);
                }
            } else if (type == List.class) {
                ListDiff listDiff = objectDiff.getListDiffs().get(name);
                if (listDiff != null) {
                    printListDiff(sb, indent, name, listDiff);
                }
            } else {
                ObjectDiff objectDiff1 = objectDiff.getObjectDiffs().get(name);
                if (objectDiff1 != null) {
                    printObjectDiff(sb, indent, name, objectDiff1);
                }
            }
        }
    }

    private void print(StringBuilder sb, ObjectDiff objectDiff, Indent indent) {
        for (Map.Entry<String, StringDiff> stringDiff : objectDiff.getStringDiffs().entrySet()) {
            String key = stringDiff.getKey();
            StringDiff value = stringDiff.getValue();
            printStringDiff(sb, indent, key, value);
        }
        for (Map.Entry<String, ObjectDiff> objectDiffEntry : objectDiff.getObjectDiffs().entrySet()) {
            printObjectDiff(sb, indent, objectDiffEntry.getKey(), objectDiffEntry.getValue());
        }
        for (Map.Entry<String, MapDiff> mapDiffEntry : objectDiff.getMapDiffs().entrySet()) {
            printMapDiff(sb, indent, mapDiffEntry.getKey(), mapDiffEntry.getValue());
        }
        for (Map.Entry<String, ListDiff> listDiffEntry : objectDiff.getListDiffs().entrySet()) {
            printListDiff(sb, indent, listDiffEntry.getKey(), listDiffEntry.getValue());
        }
        if (objectDiff.getChangeStatus() == ChangeStatus.ADDED) {
            printModel(sb, indent, objectDiff.getNewValue(), objectDiff.getChangeStatus());
        } else if (objectDiff.getChangeStatus() == ChangeStatus.REMOVED) {
            printModel(sb, indent, objectDiff.getOldValue(), objectDiff.getChangeStatus());
        }
    }

    private void printListDiff(StringBuilder sb, Indent indent, String key, ListDiff listDiff) {
        if (!listDiff.isEmpty()) {
            sb.append(indent(indent)).append(key).append(":\n");
            for (ListDiff.ListDiffEntry<?> listDiffEntry : listDiff.getListDiffsEntries()) {
                printListDiffEntries(sb, indent.incDefault(), listDiffEntry);
            }
        }
    }

    private void printListDiffEntries(StringBuilder sb, Indent indent, ListDiff.ListDiffEntry<?> listDiffEntry) {
        sb.append(indent(indent)).append("- ");
        if (listDiffEntry.getType() == ListDiff.ListDiffEntryType.STRING) {
            switch (listDiffEntry.getChangeStatus()) {
                case UNCHANGED:
                case ADDED:
                    sb.append(listDiffEntry.getNewValue()).append(changeStatus(listDiffEntry.getChangeStatus()));
                    break;
                case REMOVED:
                    sb.append(listDiffEntry.getOldValue()).append(changeStatus(listDiffEntry.getChangeStatus()));
                    break;
            }
            sb.append("\n");
        } else if (listDiffEntry.getType() == ListDiff.ListDiffEntryType.MODEL) {
            ObjectDiff objectDiff = listDiffEntry.getObjectDiff();
            if (!objectDiff.isNull()) {
                printObjectDiff(sb, indent.incListIndent().nextTimeNoIndent(), objectDiff);
            }
        }
    }

    private void printMapDiff(StringBuilder sb, Indent indent, String key, MapDiff mapDiff) {
        if (!mapDiff.isEmpty()) {
            sb.append(indent(indent)).append(key).append(":\n");
            for (Map.Entry<String, MapDiff.MapDiffEntry> mapDiffEntry : mapDiff.getMapDiffEntries().entrySet()) {
                printMapDiffEntries(sb, indent.incDefault(), mapDiffEntry.getKey(), mapDiffEntry.getValue());
            }
        }
        if (mapDiff.getChangeStatus() == ChangeStatus.ADDED) {
            sb.append(indent(indent)).append(key).append(":\n");
            for (Map.Entry<String, Model> entry : mapDiff.getNewValue().entrySet()) {
                sb.append(indent(indent.incDefault())).append(entry.getKey()).append(":\n");
                printModel(sb, indent.incDefault().incDefault(), entry.getValue(), mapDiff.getChangeStatus());
            }
        } else if (mapDiff.getChangeStatus() == ChangeStatus.REMOVED) {
            sb.append(indent(indent)).append(key).append(":\n");
            for (Map.Entry<String, Model> entry : mapDiff.getOldValue().entrySet()) {
                sb.append(indent(indent.incDefault())).append(entry.getKey()).append(":\n");
                printModel(sb, indent.incDefault().incDefault(), entry.getValue(), mapDiff.getChangeStatus());
            }
        }
    }

    private void printMapDiffEntries(StringBuilder sb, Indent indent, String key, MapDiff.MapDiffEntry mapDiffEntry) {
        sb.append(indent(indent)).append(key).append(":\n");
        ObjectDiff objectDiff = mapDiffEntry.getObjectDiff();
        if (!objectDiff.isNull()) {
            print(sb, objectDiff, indent.incDefault());
        }
    }

    private void printObjectDiff(StringBuilder sb, Indent indent, String key, ObjectDiff value) {
        if (!value.isNull()) {
            sb.append(indent(indent)).append(key).append(": ").append("\n");
            printObjectDiff(sb, indent.incDefault(), value);
        }
    }

    private void printObjectDiff(StringBuilder sb, Indent indent, ObjectDiff objectDiff) {
        if (objectDiff.getChangeStatus() == ChangeStatus.ADDED) {
            printModel(sb, indent, objectDiff.getNewValue(), objectDiff.getChangeStatus());
        } else if (objectDiff.getChangeStatus() == ChangeStatus.REMOVED) {
            printModel(sb, indent, objectDiff.getOldValue(), objectDiff.getChangeStatus());
        } else {
            print(sb, objectDiff, indent);
        }
    }

    private void printModel(StringBuilder sb, Indent indent, Model model, ChangeStatus changeStatus) {
        try {
            Class<? extends Model> aClass = model.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                Object value = field.get(model);
                if (value != null) {
                    if (String.class.isAssignableFrom(type)) {
                        sb.append(indent(indent)).append(field.getName()).append(": ").append(value).append(changeStatus(changeStatus)).append("\n");
                    } else if (Integer.class.isAssignableFrom(type)) {
                        sb.append(indent(indent)).append(field.getName()).append(": ").append(value).append(changeStatus(changeStatus)).append("\n");
                    } else if (Map.class.isAssignableFrom(type)) {
                        Map<String, Object> map = (Map<String, Object>) value;
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            Object valueMap = entry.getValue();
                            if (valueMap instanceof String) {
                                sb.append(indent(indent)).append(entry.getKey()).append(": ").append(valueMap).append(changeStatus(changeStatus)).append("\n");
                            } else if (valueMap instanceof Model) {
                                sb.append(indent(indent)).append(entry.getKey()).append(":\n");
                                printModel(sb, indent.incDefault(), (Model) valueMap, changeStatus);
                            }
                        }
                    } else if (List.class.isAssignableFrom(type)) {
                        List list = (List) value;
                        if (!list.isEmpty()) {
                            Object o = list.get(0);
                            if (o instanceof String) {
                                sb.append(indent(indent)).append(field.getName()).append(": [").append("\n");
                                for (Object s : list) {
                                    sb.append(indent(indent)).append("- ").append(s);
                                }
                                sb.append(indent(indent)).append(changeStatus(changeStatus)).append("]\n");
                            } else if (o instanceof Model) {
                                sb.append(indent(indent)).append(field.getName()).append(":").append("\n");
                                for (Object m : list) {
                                    sb.append(indent(indent.incDefault())).append("- ");
                                    printModel(sb, indent.incDefault().incListIndent().nextTimeNoIndent(), (Model) m, ChangeStatus.ADDED);
                                }
                                sb.append(indent(indent)).append(changeStatus(changeStatus)).append("]\n");
                            }
                        }
                    }
                }
            }
        } catch (SecurityException | IllegalAccessException e) {
            throw new JAsyncApiCmpTechnicalException("Failed to access field: " + e.getMessage(), e);
        }
    }

    private void printStringDiff(StringBuilder sb, Indent indent, String key, StringDiff value) {
        if (!value.isNull()) {
            sb.append(indent(indent)).append(key).append(": ");
            switch (value.getChangeStatus()) {
                case UNCHANGED:
                    sb.append(value.getOldValue()).append(changeStatus(value.getChangeStatus()));
                    break;
                case ADDED:
                    sb.append(value.getNewValue()).append(changeStatus(value.getChangeStatus()));
                    break;
                case REMOVED:
                    sb.append(value.getOldValue()).append(changeStatus(value.getChangeStatus()));
                    break;
                case CHANGED:
                    sb.append(value.getNewValue()).append(changeStatus(value.getChangeStatus(), value.getOldValue()));
                    break;
            }
            sb.append("\n");
        }
    }

    private String changeStatus(ChangeStatus changeStatus) {
        return changeStatus(changeStatus, null);
    }

    private String changeStatus(ChangeStatus changeStatus, String oldValue) {
        String retVal = " (";
        switch (changeStatus) {
            case UNCHANGED:
                retVal += "===";
                break;
            case ADDED:
                retVal += "+++";
                break;
            case REMOVED:
                retVal += "---";
                break;
            case CHANGED:
                retVal += "***";
                break;
            default:
                throw new JAsyncApiCmpTechnicalException("Unkonwn value: " + changeStatus);
        }
        if (oldValue != null) {
            retVal += " old: " + oldValue;
        }
        return retVal + ")";
    }

    private String indent(Indent indent) {
        if (indent.nextTimeNoIndent) {
            indent.nextTimeNoIndent = false;
            return "";
        }
        return " ".repeat(indent.indent);
    }
}
