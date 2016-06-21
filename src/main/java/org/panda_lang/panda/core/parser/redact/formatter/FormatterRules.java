package org.panda_lang.panda.core.parser.redact.formatter;

import org.panda_lang.panda.core.element.Literal;

import java.util.ArrayList;
import java.util.Collection;

public class FormatterRules {

    private final Collection<Literal> literals;
    private boolean whitespaceControlEnabled;
    private boolean trimEnabled;

    public FormatterRules() {
        this.literals = new ArrayList<>();
    }

    public void addLiteral(Literal literal) {
        literals.add(literal);
    }

    public void enableWhitespaceControl() {
        whitespaceControlEnabled = true;
    }

    public void enableTrim() {
        trimEnabled = true;
    }

    public void disableTrim() {
        trimEnabled = false;
    }

    public void disableWhitespaceControl() {
        whitespaceControlEnabled = false;
    }

    public boolean isTrimEnabled() {
        return trimEnabled;
    }

    public boolean isWhitespaceControlEnabled() {
        return whitespaceControlEnabled;
    }

    public Collection<Literal> getLiterals() {
        return literals;
    }

}
