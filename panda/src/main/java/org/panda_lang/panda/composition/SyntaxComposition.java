package org.panda_lang.panda.composition;

import org.panda_lang.core.interpreter.parser.lexer.suggestion.Keyword;
import org.panda_lang.core.interpreter.parser.lexer.suggestion.Separator;
import org.panda_lang.core.interpreter.parser.lexer.suggestion.Sequence;
import org.panda_lang.panda.composition.syntax.Keywords;
import org.panda_lang.panda.composition.syntax.Separators;
import org.panda_lang.panda.composition.syntax.Sequences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SyntaxComposition {

    private final Collection<Keyword> keywords;
    private final Collection<Separator> separators;
    private final Collection<Sequence> sequences;

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

    public Collection<Sequence> getSequences() {
        return sequences;
    }

    public Collection<Separator> getSeparators() {
        return separators;
    }

    public Collection<Keyword> getKeywords() {
        return keywords;
    }

}
