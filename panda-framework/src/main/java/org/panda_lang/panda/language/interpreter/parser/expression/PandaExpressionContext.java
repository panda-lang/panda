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

package org.panda_lang.panda.language.interpreter.parser.expression;

import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.token.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.interpreter.token.SynchronizedSource;

import java.util.Stack;

final public class PandaExpressionContext implements ExpressionContext {

    private final ExpressionParser parser;
    private final Context context;
    private final SourceStream source;
    private final SynchronizedSource synchronizedSource;
    private final Stack<Expression> results = new Stack<>();

    public PandaExpressionContext(ExpressionParser parser, Context context, SourceStream source) {
        this.parser = parser;
        this.context = context;
        this.source = source;
        this.synchronizedSource = new SynchronizedSource(source.toSnippet());
    }

    @Override
    public Expression popExpression() {
        return this.getResults().pop();
    }

    @Override
    public Expression peekExpression() {
        return this.getResults().peek();
    }

    @Override
    public boolean hasResults() {
        return !this.getResults().isEmpty();
    }

    @Override
    public SynchronizedSource getSynchronizedSource() {
        return synchronizedSource;
    }

    @Override
    public SourceStream getSource() {
        return source;
    }

    @Override
    public Stack<Expression> getResults() {
        return results;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public ExpressionParser getParser() {
        return parser;
    }

}

