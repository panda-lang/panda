package org.panda_lang.panda.core.interpreter.parser.redact.divider;

import org.panda_lang.panda.core.element.Sequence;
import org.panda_lang.panda.core.element.Separator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Set of rules for {@link Divider}
 */
public class DividerRules {

    private final Collection<Separator> separators;
    private final Collection<Sequence> sequences;
    private boolean omissionsEnabled;

    public DividerRules() {
        this.separators = new ArrayList<>();
        this.sequences = new ArrayList<>();
    }

    public void enableOmissions() {
        omissionsEnabled = true;
    }

    public void addLineSeparator(Separator separator) {
        separators.add(separator);
    }

    public void addLiteral(Sequence sequence) {
        sequences.add(sequence);
    }

    public boolean isOmissionsEnabled() {
        return omissionsEnabled;
    }

    public Collection<Sequence> getSequences() {
        return sequences;
    }

    public Collection<Separator> getSeparators() {
        return separators;
    }

}
