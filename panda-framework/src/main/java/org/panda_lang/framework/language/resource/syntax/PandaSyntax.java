/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.framework.language.resource.syntax;

import org.panda_lang.framework.design.resource.Syntax;
import org.panda_lang.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.framework.language.resource.syntax.keyword.Keyword;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.framework.language.resource.syntax.literal.Literal;
import org.panda_lang.framework.language.resource.syntax.literal.Literals;
import org.panda_lang.framework.language.resource.syntax.operator.Operator;
import org.panda_lang.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.framework.language.resource.syntax.separator.Separator;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.framework.language.resource.syntax.sequence.Sequence;
import org.panda_lang.framework.language.resource.syntax.sequence.Sequences;
import org.panda_lang.utilities.commons.collection.Lists;

import java.util.Arrays;
import java.util.List;

public class PandaSyntax implements Syntax {

    private final List<Keyword> keywords = Arrays.asList(Keywords.values());
    private final List<Literal> literals = Arrays.asList(Literals.values());
    private final List<Operator> operators = Arrays.asList(Operators.values());
    private final List<Sequence> sequences = Arrays.asList(Sequences.values());
    private final List<Separator> separators = Arrays.asList(Separators.values());
    private final char[] specialCharacters = DefaultCharacters.getSpecialCharacters();

    public PandaSyntax() {
        this.sort();
    }

    public void sort() {
        Lists.sort(TokenUtils.TOKEN_ORDER_COMPARATOR, keywords, literals, separators, operators, sequences);
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

}
