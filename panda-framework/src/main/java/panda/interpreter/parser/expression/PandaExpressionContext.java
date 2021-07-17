/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.parser.expression;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.Contextual;
import panda.interpreter.token.Snippet;
import panda.interpreter.token.Snippetable;
import panda.interpreter.token.SynchronizedSource;

import java.util.Stack;

final public class PandaExpressionContext<T> implements ExpressionContext<T> {

    private final ExpressionParser parser;
    private final Context<T> context;
    private final SynchronizedSource synchronizedSource;
    private final Stack<Expression> results = new Stack<>();
    private final Stack<ExpressionResult> errors = new Stack<>();

    public PandaExpressionContext(ExpressionParser parser, Contextual<T> context, Snippetable source) {
        this.parser = parser;
        this.context = context.toContext();
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
    public Stack<ExpressionResult> getErrors() {
        return errors;
    }

    @Override
    public Stack<Expression> getResults() {
        return results;
    }

    @Override
    public Snippet toSnippet() {
        return getSynchronizedSource().toSnippet();
    }

    @Override
    public Context<T> toContext() {
        return context;
    }

    @Override
    public ExpressionParser getParser() {
        return parser;
    }

}

