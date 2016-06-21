package org.panda_lang.panda.core.parser.redact.divider;

import org.panda_lang.panda.core.element.Literal;
import org.panda_lang.panda.core.element.Separator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Set of rules for {@link Divider}
 */
public class DividerRules {

    private final Collection<Separator> separators;
    private final Collection<Literal> literals;

    public DividerRules() {
        this.separators = new ArrayList<>();
        this.literals = new ArrayList<>();
    }

    public void addLineSeparator(Separator separator) {
        separators.add(separator);
    }

    public void addLiteral(Literal literal) {
        literals.add(literal);
    }

    public Collection<Literal> getLiterals() {
        return literals;
    }

    public Collection<Separator> getSeparators() {
        return separators;
    }

}
