package org.panda_lang.panda.composition;

import org.panda_lang.panda.composition.syntax.Keywords;
import org.panda_lang.panda.composition.syntax.Separators;
import org.panda_lang.panda.composition.syntax.Sequences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SyntaxComposition {

    private final Collection<String> keywords;
    private final Collection<String> separators;
    private final Collection<String> sequences;

    public SyntaxComposition() {
        this.keywords = new ArrayList<>();
        this.separators = new ArrayList<>();
        this.sequences = new ArrayList<>();

        this.initialize();
    }

    protected void initialize() {
        Collections.addAll(keywords, Keywords.values());
        Collections.addAll(separators, Separators.values());
        Collections.addAll(sequences, Sequences.values());
    }

    public Collection<String> getSequences() {
        return sequences;
    }

    public Collection<String> getSeparators() {
        return separators;
    }

    public Collection<String> getKeywords() {
        return keywords;
    }

}
