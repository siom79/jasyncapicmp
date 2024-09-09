package jasyncapicmp.util;

import java.util.Stack;

public class Path {
	private final Stack<String> stack = new Stack<>();

	public Path down(String level) {
		this.stack.add(level);
		return this;
	}

	public Path up() {
		this.stack.pop();
		return this;
	}

	@Override
	public String toString() {
		return String.join(".", this.stack);
	}
}
