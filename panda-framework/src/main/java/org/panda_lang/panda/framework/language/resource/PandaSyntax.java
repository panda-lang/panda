/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.resource;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.resource.Syntax;
import org.panda_lang.panda.framework.language.resource.syntax.DefaultCharacters;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keyword;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.framework.language.resource.syntax.literal.Literal;
import org.panda_lang.panda.framework.language.resource.syntax.literal.Literals;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operator;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.framework.language.resource.syntax.sequence.Sequence;
import org.panda_lang.panda.framework.language.resource.syntax.sequence.Sequences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PandaSyntax implements Syntax {

    private static final PandaSyntax INSTANCE = new PandaSyntax();

    private final List<Keyword> keywords;
    private final List<Literal> literals;
    private final List<Separator> separators;
    private final List<Operator> operators;
    private final List<Sequence> sequences;
    private char[] specialCharacters;

    private PandaSyntax() {
        this.keywords = new ArrayList<>();
        this.literals = new ArrayList<>();
        this.separators = new ArrayList<>();
        this.operators = new ArrayList<>();
        this.sequences = new ArrayList<>();
        this.specialCharacters = DefaultCharacters.getSpecialCharacters();

        this.initialize();
    }

    private void initialize() {
        Collections.addAll(keywords, Keywords.values());
        Collections.addAll(literals, Literals.values());
        Collections.addAll(separators, Separators.values());
        Collections.addAll(operators, Operators.values());
        Collections.addAll(sequences, Sequences.values());

        Comparator<Token> tokenComparator = (x, y) -> Integer.compare(y.getTokenValue().length(), x.getTokenValue().length());

        keywords.sort(tokenComparator);
        literals.sort(tokenComparator);
        separators.sort(tokenComparator);
        operators.sort(tokenComparator);
        sequences.sort(tokenComparator);
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
    public List<Literal> getLiterals() {
        return literals;
    }

    @Override
    public List<Keyword> getKeywords() {
        return keywords;
    }

    public static PandaSyntax getInstance() {
        return INSTANCE;
    }

}
