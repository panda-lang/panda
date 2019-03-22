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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

import java.util.Stack;

public class ExpressionContext {

    private final ExpressionParser parser;
    private final ParserData data;
    private final SourceStream source;

    private final Stack<Expression> results;
    private final TokenReader reader;
    private TokenRepresentation next;

    public ExpressionContext(ExpressionParser parser, ParserData data, SourceStream source, Stack<Expression> results, TokenReader reader) {
        this.parser = parser;
        this.data = data;
        this.source = source;
        this.results = results;
        this.reader = reader;
    }

    protected ExpressionContext withUpdatedToken(TokenRepresentation next) {
        this.next = next;
        return this;
    }

    public Expression popExpression() {
        return this.getResults().pop();
    }

    public Expression peekExpression() {
        return this.getResults().peek();
    }

    public boolean hasResults() {
        return !this.getResults().isEmpty();
    }

    public TokenRepresentation getNext() {
        return next;
    }

    public TokenReader getReader() {
        return reader;
    }

    public SourceStream getSource() {
        return source;
    }

    public Stack<Expression> getResults() {
        return results;
    }

    public ParserData getData() {
        return data;
    }

    public ExpressionParser getParser() {
        return parser;
    }

}
