package jasyncapicmp.output;

import jasyncapicmp.cmp.ChangeStatus;

public interface OutputSink {
    void stringDiff(Indent indent, String key, String oldValue, String newValue, ChangeStatus changeStatus);

    void stringDiff(Indent indent, String name, String value, ChangeStatus changeStatus);

    void mapDiffStart(Indent indent, String key, ChangeStatus changeStatus);

    void mapDiffEntry(Indent indent, String key, ChangeStatus changeStatus);

    void listDiffStart(Indent indent, String key, ChangeStatus changeStatus);

    void listDiffEntryString(Indent indent, Object s, ChangeStatus changeStatus);

    void listDiffEntryMap(Indent incDefault);

    void listDiffModelStart(Indent indent);

    void objectDiffStart(Indent indent, String key, ChangeStatus changeStatus);
}
