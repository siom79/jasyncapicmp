package jasyncapicmp.output;

import jasyncapicmp.cmp.ChangeStatus;
import jasyncapicmp.cmp.compat.HasCompatibilityChanges;

public interface OutputSink {
    void stringDiff(Indent indent, String key, String oldValue, String newValue, ChangeStatus changeStatus, HasCompatibilityChanges hasCompatibilityChanges);

    void stringDiff(Indent indent, String name, String value, ChangeStatus changeStatus, HasCompatibilityChanges hasCompatibilityChanges);

	void integerDiff(Indent indent, String name, Integer oldValue, Integer newValue, ChangeStatus changeStatus, HasCompatibilityChanges hasCompatibilityChanges);

	void mapDiffStart(Indent indent, String key, ChangeStatus changeStatus);

	void mapDiffEntry(Indent indent, String key, ChangeStatus changeStatus, HasCompatibilityChanges hasCompatibilityChanges);

	void listDiffStart(Indent indent, String key, ChangeStatus changeStatus);

	void listDiffEntryString(Indent indent, Object s, ChangeStatus changeStatus, HasCompatibilityChanges hasCompatibilityChanges);

	void listDiffEntryMap(Indent incDefault);

    void listDiffModelStart(Indent indent);

    void objectDiffStart(Indent indent, String key, ChangeStatus changeStatus);
}
