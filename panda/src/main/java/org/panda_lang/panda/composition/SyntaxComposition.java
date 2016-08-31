package org.panda_lang.panda.composition;

import org.panda_lang.core.interpreter.token.Token;
import org.panda_lang.core.interpreter.token.suggestion.Keyword;
import org.panda_lang.core.interpreter.token.suggestion.Operator;
import org.panda_lang.core.interpreter.token.suggestion.Separator;
import org.panda_lang.core.interpreter.token.suggestion.Sequence;
import org.panda_lang.panda.composition.syntax.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SyntaxComposition {

    private final List<Keyword> keywords;
    private final List<Separator> separators;
    private final List<Operator> operators;
    private final List<Sequence> sequences;
    private char[] specialCharacters;

    public SyntaxComposition() {
        this.keywords = new ArrayList<>();
        this.separators = new ArrayList<>();
        this.operators = new ArrayList<>();
        this.sequences = new ArrayList<>();
        this.specialCharacters = Characters.getSpecialCharacters();

        this.initialize();
    }

    protected void initialize() {
        Collections.addAll(keywords, Keywords.values());
        Collections.addAll(separators, Separators.values());
        Collections.addAll(operators, Operators.values());
        Collections.addAll(sequences, Sequences.values());

        Comparator<Token> tokenComparator = new Comparator<Token>() {
            @Override
            public int compare(Token t, Token t1) {
                return Integer.compare(t1.getToken().length(), t.getToken().length());
            }
        };

        Collections.sort(keywords, tokenComparator);
        Collections.sort(separators, tokenComparator);
        Collections.sort(operators, tokenComparator);
        Collections.sort(sequences, tokenComparator);
    }

    public char[] getSpecialCharacters() {
        return specialCharacters;
    }

    public void setSpecialCharacters(char[] specialCharacters) {
        this.specialCharacters = specialCharacters;
    }

    public List<Sequence> getSequences() {
        return sequences;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public List<Separator> getSeparators() {
        return separators;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

}
