package jasyncapicmp.output;

import lombok.ToString;

@ToString
class Indent {
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
