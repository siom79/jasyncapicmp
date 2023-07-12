package jasyncapicmp.output;

import jasyncapicmp.JAsyncApiCmpTechnicalException;
import jasyncapicmp.cmp.ChangeStatus;
import jasyncapicmp.cmp.diff.ListDiff;
import jasyncapicmp.cmp.diff.MapDiff;
import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.cmp.diff.StringDiff;
import jasyncapicmp.model.AsyncApi;
import jasyncapicmp.model.Model;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class OutputProcessor {

    private final OutputSink ot;

    public OutputProcessor(OutputSink outputSink) {
        this.ot = outputSink;
    }

    public void process(ObjectDiff objectDiff) {
        printTopLevel(ot, objectDiff, new Indent(0, false));
    }

    private void printTopLevel(OutputSink ot, ObjectDiff objectDiff, Indent indent) {
        Field[] declaredFields = AsyncApi.class.getDeclaredFields();
        for (Field field : declaredFields) {
            Class<?> type = field.getType();
            String name = field.getName();
            if (type == String.class) {
                StringDiff stringDiff = objectDiff.getStringDiffs().get(name);
                if (stringDiff != null) {
                    printStringDiff(ot, indent, name, stringDiff);
                }
            } else if (type == Map.class) {
                MapDiff mapDiff = objectDiff.getMapDiffs().get(name);
                if (mapDiff != null) {
                    printMapDiff(ot, indent, name, mapDiff);
                }
            } else if (type == List.class) {
                ListDiff listDiff = objectDiff.getListDiffs().get(name);
                if (listDiff != null) {
                    printListDiff(ot, indent, name, listDiff);
                }
            } else {
                ObjectDiff objectDiff1 = objectDiff.getObjectDiffs().get(name);
                if (objectDiff1 != null) {
                    printObjectDiff(ot, indent, name, objectDiff1);
                }
            }
        }
    }

    private void process(OutputSink ot, ObjectDiff objectDiff, Indent indent) {
        for (Map.Entry<String, StringDiff> stringDiff : objectDiff.getStringDiffs().entrySet()) {
            String key = stringDiff.getKey();
            StringDiff value = stringDiff.getValue();
            printStringDiff(ot, indent, key, value);
        }
        for (Map.Entry<String, ObjectDiff> objectDiffEntry : objectDiff.getObjectDiffs().entrySet()) {
            printObjectDiff(ot, indent, objectDiffEntry.getKey(), objectDiffEntry.getValue());
        }
        for (Map.Entry<String, MapDiff> mapDiffEntry : objectDiff.getMapDiffs().entrySet()) {
            printMapDiff(ot, indent, mapDiffEntry.getKey(), mapDiffEntry.getValue());
        }
        for (Map.Entry<String, ListDiff> listDiffEntry : objectDiff.getListDiffs().entrySet()) {
            printListDiff(ot, indent, listDiffEntry.getKey(), listDiffEntry.getValue());
        }
        if (objectDiff.getChangeStatus() == ChangeStatus.ADDED) {
            printModel(ot, indent, objectDiff.getNewValue(), objectDiff.getChangeStatus());
        } else if (objectDiff.getChangeStatus() == ChangeStatus.REMOVED) {
            printModel(ot, indent, objectDiff.getOldValue(), objectDiff.getChangeStatus());
        }
    }

    private void printListDiff(OutputSink ot, Indent indent, String key, ListDiff listDiff) {
        if (!listDiff.isEmpty()) {
            ot.listDiffStart(indent, key, listDiff.getChangeStatus());
            for (ListDiff.ListDiffEntry<?> listDiffEntry : listDiff.getListDiffsEntries()) {
                printListDiffEntries(ot, indent.incDefault(), listDiffEntry);
            }
        }
    }

    private void printListDiffEntries(OutputSink ot, Indent indent, ListDiff.ListDiffEntry<?> listDiffEntry) {
        if (listDiffEntry.getType() == ListDiff.ListDiffEntryType.STRING) {
            switch (listDiffEntry.getChangeStatus()) {
                case UNCHANGED, ADDED ->
                        ot.listDiffEntryString(indent, listDiffEntry.getNewValue(), listDiffEntry.getChangeStatus(), listDiffEntry);
                case REMOVED ->
                        ot.listDiffEntryString(indent, listDiffEntry.getOldValue(), listDiffEntry.getChangeStatus(), listDiffEntry);
            }
        } else if (listDiffEntry.getType() == ListDiff.ListDiffEntryType.MODEL) {
            ot.listDiffModelStart(indent);
            ObjectDiff objectDiff = listDiffEntry.getObjectDiff();
            if (!objectDiff.isNull()) {
                printObjectDiff(ot, indent.incListIndent().nextTimeNoIndent(), objectDiff);
            }
        }
    }

    private void printMapDiff(OutputSink ot, Indent indent, String key, MapDiff mapDiff) {
        if (!mapDiff.isEmpty()) {
            ot.mapDiffStart(indent, key, mapDiff.getChangeStatus());
            for (Map.Entry<String, MapDiff.MapDiffEntry> mapDiffEntry : mapDiff.getMapDiffEntries().entrySet()) {
                printMapDiffEntries(ot, indent.incDefault(), mapDiffEntry.getKey(), mapDiffEntry.getValue());
            }
        }
    }

    private void printMapDiffEntries(OutputSink ot, Indent indent, String key, MapDiff.MapDiffEntry mapDiffEntry) {
        ot.mapDiffEntry(indent, key, mapDiffEntry.getChangeStatus(), mapDiffEntry);
        ObjectDiff objectDiff = mapDiffEntry.getObjectDiff();
        if (!objectDiff.isNull()) {
            process(ot, objectDiff, indent.incDefault());
        }
    }

    private void printObjectDiff(OutputSink ot, Indent indent, String key, ObjectDiff objectDiff) {
        if (!objectDiff.isNull()) {
            ot.objectDiffStart(indent, key, objectDiff.getChangeStatus());
            printObjectDiff(ot, indent.incDefault(), objectDiff);
        }
    }

    private void printObjectDiff(OutputSink ot, Indent indent, ObjectDiff objectDiff) {
        if (objectDiff.getChangeStatus() == ChangeStatus.ADDED) {
            printModel(ot, indent, objectDiff.getNewValue(), objectDiff.getChangeStatus());
        } else if (objectDiff.getChangeStatus() == ChangeStatus.REMOVED) {
            printModel(ot, indent, objectDiff.getOldValue(), objectDiff.getChangeStatus());
        } else {
            process(ot, objectDiff, indent);
        }
    }

    private void printModel(OutputSink ot, Indent indent, Model model, ChangeStatus changeStatus) {
        try {
            Class<? extends Model> aClass = model.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                Object value = field.get(model);
                if (value != null) {
                    if (String.class.isAssignableFrom(type)) {
                        ot.stringDiff(indent, field.getName(), (String) value, changeStatus, null);
                    } else if (Integer.class.isAssignableFrom(type)) {
                        ot.stringDiff(indent, field.getName(), value.toString(), changeStatus, null);
                    } else if (Map.class.isAssignableFrom(type)) {
                        Map<String, Object> map = (Map<String, Object>) value;
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            Object entryValue = entry.getValue();
                            if (entryValue instanceof String) {
                                ot.stringDiff(indent, entry.getKey(), (String) entryValue, changeStatus, null);
                            } else if (entryValue instanceof Model) {
                                ot.stringDiff(indent, entry.getKey(), entryValue.toString(), changeStatus, null);
                                printModel(ot, indent.incDefault(), (Model) entryValue, changeStatus);
                            }
                        }
                    } else if (List.class.isAssignableFrom(type)) {
                        List list = (List) value;
                        if (!list.isEmpty()) {
                            Object o = list.get(0);
                            if (o instanceof String) {
                                ot.listDiffStart(indent, field.getName(), changeStatus);
                                for (Object s : list) {
                                    ot.listDiffEntryString(indent, s, changeStatus, null);
                                }
                            } else if (o instanceof Model) {
                                ot.listDiffStart(indent, field.getName(), changeStatus);
                                for (Object m : list) {
                                    ot.listDiffEntryMap(indent.incDefault());
                                    printModel(ot, indent.incDefault().incListIndent().nextTimeNoIndent(), (Model) m, ChangeStatus.ADDED);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SecurityException | IllegalAccessException e) {
            throw new JAsyncApiCmpTechnicalException("Failed to access field: " + e.getMessage(), e);
        }
    }

    private void printStringDiff(OutputSink ot, Indent indent, String key, StringDiff value) {
        if (!value.isNull()) {
            ot.stringDiff(indent, key, value.getOldValue(), value.getNewValue(), value.getChangeStatus(), value);
        }
    }
}
