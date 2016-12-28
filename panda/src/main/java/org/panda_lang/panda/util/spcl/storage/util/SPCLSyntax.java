/*
 * Copyright (c) 2015-2016 Dzikoysk
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

package org.panda_lang.panda.util.spcl.storage.util;

import org.panda_lang.framework.composition.Syntax;
import org.panda_lang.framework.interpreter.token.suggestion.Keyword;
import org.panda_lang.framework.interpreter.token.suggestion.Operator;
import org.panda_lang.framework.interpreter.token.suggestion.Separator;
import org.panda_lang.framework.interpreter.token.suggestion.Sequence;
import org.panda_lang.panda.implementation.syntax.Sequences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SPCLSyntax implements Syntax {

    private final char[] specialCharacters;
    private final List<Sequence> sequences;
    private final List<Operator> operators;
    private final List<Separator> separators;
    private final List<Keyword> keywords;

    public SPCLSyntax() {
        this.specialCharacters = new char[0];
        this.keywords = new ArrayList<>(0);

        this.operators = Collections.singletonList(SPCLTokens.VAR);
        this.sequences = Arrays.asList(Sequences.CHARACTER, Sequences.STRING, Sequences.LINE_ORIENTED_COMMENT, SPCLTokens.HASH_COMMENT);
        this.separators = Arrays.asList(SPCLTokens.COLON, SPCLTokens.DASH, SPCLTokens.LINE);
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
