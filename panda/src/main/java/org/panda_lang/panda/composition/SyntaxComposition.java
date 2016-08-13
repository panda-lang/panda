package org.panda_lang.panda.composition;

import org.panda_lang.core.interpreter.parser.lexer.Token;
import org.panda_lang.core.interpreter.parser.lexer.suggestion.Keyword;
import org.panda_lang.core.interpreter.parser.lexer.suggestion.Separator;
import org.panda_lang.core.interpreter.parser.lexer.suggestion.Sequence;
import org.panda_lang.panda.composition.syntax.Keywords;
import org.panda_lang.panda.composition.syntax.Separators;
import org.panda_lang.panda.composition.syntax.Sequences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SyntaxComposition {

    private final List<Keyword> keywords;
    private final List<Separator> separators;
    private final List<Sequence> sequences;

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

        Comparator<Token> tokenComparator = new Comparator<Token>() {
            @Override
            public int compare(Token t, Token t1) {
                return Integer.compare(t.getToken().length(), t1.getToken().length());
            }
        };

        Collections.sort(separators, tokenComparator);
        Collections.sort(separators, tokenComparator);
        Collections.sort(separators, tokenComparator);
    }

    public List<Sequence> getSequences() {
        return sequences;
    }

    public List<Separator> getSeparators() {
        return separators;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

}
