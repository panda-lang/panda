package org.panda_lang.panda.core.element;

public class Sequence implements Token {

    private final String[] range;
    private String name;
    private boolean overlooked;

    public Sequence(char range) {
        this(Character.toString(range));
    }

    public Sequence(String range) {
        this(new String[]{ range, range });
    }

    public Sequence(String literalStart, String literalEnd) {
        this(new String[]{ literalStart, literalEnd });
    }

    public Sequence(String[] range) {
        if (range.length == 0) {
            this.range = new String[]{ "", "" };
        }
        else if (range.length == 1) {
            this.range = new String[]{ range[0], range[0] };
        }
        else {
            this.range = range;
        }
    }

    public Sequence name(String name) {
        this.name = name;
        return this;
    }

    public Sequence overlooked(boolean overlooked) {
        this.overlooked = overlooked;
        return this;
    }

    public boolean isOverlooked() {
        return overlooked;
    }

    @Override
    public String getToken() {
        return getSequenceStart() + getSequenceEnd();
    }

    public String getSequenceStart() {
        return range[0];
    }

    public String getSequenceEnd() {
        return range[1];
    }

    public String getName() {
        return name;
    }

    public String[] getRange() {
        return range;
    }

}
