package jasyncapicmp.output;

import jasyncapicmp.JAsyncApiCmpTechnicalException;
import jasyncapicmp.cmp.ChangeStatus;

public class StdoutOutputSink implements OutputSink {
    private final StringBuilder sb;

    public StdoutOutputSink() {
        this.sb = new StringBuilder();
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    @Override
    public void stringDiff(Indent indent, String key, String oldValue, String newValue, ChangeStatus changeStatus) {
        sb.append(indent(indent)).append(key).append(": ");
        switch (changeStatus) {
            case UNCHANGED, REMOVED -> sb.append(oldValue).append(changeStatus(changeStatus));
            case ADDED -> sb.append(newValue).append(changeStatus(changeStatus));
            case CHANGED -> sb.append(newValue).append(changeStatus(changeStatus, oldValue));
        }
        sb.append("\n");
    }

    @Override
    public void stringDiff(Indent indent, String name, String value, ChangeStatus changeStatus) {
        sb.append(indent(indent)).append(name).append(": ").append(value).append(changeStatus(changeStatus)).append("\n");
    }

    @Override
    public void mapDiffStart(Indent indent, String key, ChangeStatus changeStatus) {
        sb.append(indent(indent)).append(key).append(":").append(changeStatus(changeStatus)).append("\n");
    }

    @Override
    public void mapDiffEntry(Indent indent, String key, ChangeStatus changeStatus) {
        sb.append(indent(indent)).append(key).append(":").append(changeStatus(changeStatus)).append("\n");
    }

    @Override
    public void listDiffStart(Indent indent, String key, ChangeStatus changeStatus) {
        sb.append(indent(indent)).append(key).append(":").append(changeStatus(changeStatus)).append("\n");
    }

    @Override
    public void listDiffEntryString(Indent indent, Object s, ChangeStatus changeStatus) {
        sb.append(indent(indent)).append("- ").append(s).append(changeStatus(changeStatus)).append("\n");
    }

    @Override
    public void listDiffEntryMap(Indent indent) {
        sb.append(indent(indent)).append("- ");
    }

    @Override
    public void listDiffModelStart(Indent indent) {
        sb.append(indent(indent)).append("- ");
    }

    @Override
    public void objectDiffStart(Indent indent, String key, ChangeStatus changeStatus) {
        sb.append(indent(indent)).append(key).append(":").append(changeStatus(changeStatus)).append("\n");
    }

    private String indent(Indent indent) {
        if (indent.nextTimeNoIndent) {
            indent.nextTimeNoIndent = false;
            return "";
        }
        return " ".repeat(indent.indent);
    }

    private String changeStatus(ChangeStatus changeStatus) {
        return changeStatus(changeStatus, null);
    }

    private String changeStatus(ChangeStatus changeStatus, String oldValue) {
        String retVal = " (";
        switch (changeStatus) {
            case UNCHANGED -> retVal += "===";
            case ADDED -> retVal += "+++";
            case REMOVED -> retVal += "---";
            case CHANGED -> retVal += "***";
            default -> throw new JAsyncApiCmpTechnicalException("Unkonwn value: " + changeStatus);
        }
        if (oldValue != null) {
            retVal += " old: " + oldValue;
        }
        return retVal + ")";
    }
}
