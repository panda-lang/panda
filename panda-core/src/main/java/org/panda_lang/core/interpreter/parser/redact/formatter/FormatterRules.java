package org.panda_lang.core.interpreter.parser.redact.formatter;

import org.panda_lang.core.interpreter.parser.redact.Sequence;

import java.util.ArrayList;
import java.util.Collection;

public class FormatterRules {

    private final Collection<Sequence> sequences;
    private boolean sequenceOverlookEnabled;
    private boolean whitespaceControlEnabled;
    private boolean trimEnabled;

    public FormatterRules() {
        this.sequences = new ArrayList<>();
    }

    public void addLiteral(Sequence sequence) {
        sequences.add(sequence);
    }

    public void enableSequenceOverlook() {
        sequenceOverlookEnabled = true;
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

    public void disableSequenceOverlook() {
        sequenceOverlookEnabled = false;
    }

    public boolean isTrimEnabled() {
        return trimEnabled;
    }

    public boolean isWhitespaceControlEnabled() {
        return whitespaceControlEnabled;
    }

    public boolean isSequenceOverlookEnabled() {
        return sequenceOverlookEnabled;
    }

    public Collection<Sequence> getSequences() {
        return sequences;
    }

}
