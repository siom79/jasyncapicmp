package jasyncapicmp.output;

import jasyncapicmp.JAsyncApiCmpTechnicalException;
import jasyncapicmp.cmp.ChangeStatus;
import jasyncapicmp.cmp.compat.HasCompatibilityChanges;

import java.util.stream.Collectors;

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
	public void stringDiff(Indent indent, String key, String oldValue, String newValue, ChangeStatus changeStatus, HasCompatibilityChanges hasCompatibilityChanges) {
		sb.append(indent(indent)).append(key).append(": ");
		switch (changeStatus) {
			case UNCHANGED, REMOVED -> sb.append(oldValue).append(changeStatus(changeStatus, hasCompatibilityChanges));
			case ADDED -> sb.append(newValue).append(changeStatus(changeStatus, hasCompatibilityChanges));
			case CHANGED -> sb.append(newValue).append(changeStatus(changeStatus, oldValue, hasCompatibilityChanges));
		}
		sb.append("\n");
	}

	@Override
	public void stringDiff(Indent indent, String name, String value, ChangeStatus changeStatus, HasCompatibilityChanges hasCompatibilityChanges) {
		sb.append(indent(indent)).append(name).append(": ").append(value).append(changeStatus(changeStatus, hasCompatibilityChanges)).append("\n");
	}

	@Override
	public void mapDiffStart(Indent indent, String key, ChangeStatus changeStatus) {
		sb.append(indent(indent)).append(key).append(":").append(changeStatus(changeStatus, null)).append("\n");
	}

	@Override
	public void mapDiffEntry(Indent indent, String key, ChangeStatus changeStatus, HasCompatibilityChanges hasCompatibilityChanges) {
		sb.append(indent(indent)).append(key).append(":").append(changeStatus(changeStatus, hasCompatibilityChanges)).append("\n");
	}

	@Override
	public void listDiffStart(Indent indent, String key, ChangeStatus changeStatus) {
		sb.append(indent(indent)).append(key).append(":").append(changeStatus(changeStatus, null)).append("\n");
	}

	@Override
	public void listDiffEntryString(Indent indent, Object s, ChangeStatus changeStatus, HasCompatibilityChanges hasCompatibilityChanges) {
		sb.append(indent(indent)).append("- ").append(s).append(changeStatus(changeStatus, hasCompatibilityChanges)).append("\n");
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
		sb.append(indent(indent)).append(key).append(":").append(changeStatus(changeStatus, null)).append("\n");
	}

	private String indent(Indent indent) {
		if (indent.nextTimeNoIndent) {
			indent.nextTimeNoIndent = false;
			return "";
		}
		return " ".repeat(indent.indent);
	}

	private String changeStatus(ChangeStatus changeStatus, HasCompatibilityChanges hasCompatibilityChanges) {
		return changeStatus(changeStatus, null, hasCompatibilityChanges);
	}

	private String changeStatus(ChangeStatus changeStatus, String oldValue, HasCompatibilityChanges hasCompatibilityChanges) {
		boolean compatibilityChanges = false;
		if (hasCompatibilityChanges != null) {
			compatibilityChanges = hasCompatibilityChanges.getApiCompatibilityChanges().stream()
					.anyMatch(acc -> !acc.isBackwardCompatible() || !acc.isForwardCompatible());
		}
		String retVal = " (";
		if (compatibilityChanges) {
			retVal += "!";
		}
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
		if (hasCompatibilityChanges != null && !hasCompatibilityChanges.getApiCompatibilityChanges().isEmpty()) {
			retVal += ", compatibility change" + (hasCompatibilityChanges.getApiCompatibilityChanges().size() > 1 ? "s" : "") + ": ";
			retVal += hasCompatibilityChanges.getApiCompatibilityChanges().stream().map(Enum::name).collect(Collectors.joining(","));
		}
		return retVal + ")";
	}
}
