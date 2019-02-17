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

package org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.utils.reader;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;

import java.util.function.Predicate;

public class ExpressionSeparatorExtensions {

    protected final ReaderFinisher finisher;
    protected final @Nullable Predicate<Token> tokenFilter;
    protected final @Nullable Predicate<Tokens> sectionFilter;

    public ExpressionSeparatorExtensions(ReaderFinisher finisher, @Nullable Predicate<Token> tokenFilter, @Nullable Predicate<Tokens> sectionFilter) {
        this.finisher = finisher;
        this.tokenFilter = tokenFilter;
        this.sectionFilter = sectionFilter;
    }

    public ExpressionSeparatorExtensions(ReaderFinisher finisher, @Nullable Predicate<Token> tokenFilter) {
        this(finisher, tokenFilter, null);
    }

    public ExpressionSeparatorExtensions(ReaderFinisher finisher) {
        this(finisher, null, null);
    }

    protected boolean testToken(Token token) {
        return tokenFilter != null && tokenFilter.test(token);
    }

    protected boolean testSection(Tokens tokens) {
        return sectionFilter != null && sectionFilter.test(tokens);
    }

}
