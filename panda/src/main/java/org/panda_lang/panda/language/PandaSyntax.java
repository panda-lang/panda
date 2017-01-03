/*
 * Copyright (c) 2015-2017 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.language;

import org.panda_lang.framework.composition.Syntax;
import org.panda_lang.framework.interpreter.token.Token;
import org.panda_lang.framework.interpreter.token.suggestion.Keyword;
import org.panda_lang.framework.interpreter.token.suggestion.Operator;
import org.panda_lang.framework.interpreter.token.suggestion.Separator;
import org.panda_lang.framework.interpreter.token.suggestion.Sequence;
import org.panda_lang.panda.language.syntax.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PandaSyntax implements Syntax {

    private final List<Keyword> keywords;
    private final List<Separator> separators;
    private final List<Operator> operators;
    private final List<Sequence> sequences;
    private char[] specialCharacters;

    public PandaSyntax() {
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
                return Integer.compare(t1.getTokenValue().length(), t.getTokenValue().length());
            }
        };

        Collections.sort(keywords, tokenComparator);
        Collections.sort(separators, tokenComparator);
        Collections.sort(operators, tokenComparator);
        Collections.sort(sequences, tokenComparator);
    }

    public void setSpecialCharacters(char[] specialCharacters) {
        this.specialCharacters = specialCharacters;
    }

    @Override
    public char[] getSpecialCharacters() {
        return specialCharacters;
    }

    @Override
    public List<Sequence> getSequences() {
        return sequences;
    }

    @Override
    public List<Operator> getOperators() {
        return operators;
    }

    @Override
    public List<Separator> getSeparators() {
        return separators;
    }

    @Override
    public List<Keyword> getKeywords() {
        return keywords;
    }

}
