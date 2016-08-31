package org.panda_lang.core.util.redact.divider;

import org.panda_lang.core.interpreter.token.suggestion.Separator;
import org.panda_lang.core.interpreter.token.suggestion.Sequence;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Set of rules for Divider
 *
 * @see Divider
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

    public void addSequence(Sequence sequence) {
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
