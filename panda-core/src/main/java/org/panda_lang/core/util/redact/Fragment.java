package org.panda_lang.core.util.redact;

import org.panda_lang.core.interpreter.lexer.suggestion.Separator;

public class Fragment {

    private String fragment;
    private String indention;
    private Separator separator;
    private int inline;
    private int line;

    public Fragment() {
    }

    public Fragment(String fragment) {
        this.fragment = fragment;
    }

    public boolean isEmpty() {
        return fragment.isEmpty();
    }

    public String getFull() {
        return getIndention() + getFragment();
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getInline() {
        return inline;
    }

    public void setInline(int inline) {
        this.inline = inline;
    }

    public Separator getSeparator() {
        return separator;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }

    public String getIndention() {
        return indention;
    }

    public void setIndention(String indention) {
        this.indention = indention;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

}
