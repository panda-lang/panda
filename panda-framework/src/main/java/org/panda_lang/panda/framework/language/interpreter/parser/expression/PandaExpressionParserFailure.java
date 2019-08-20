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

package org.panda_lang.panda.framework.language.interpreter.parser.expression;

import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;

public class PandaExpressionParserFailure extends PandaParserFailure {

    private final String expressionMessage;

    public PandaExpressionParserFailure(String prefix, String message, ExpressionContext context, Snippet source) {
        super(builder(message, context.getContext()).withStreamOrigin(source));
        this.expressionMessage = message;
    }

    public PandaExpressionParserFailure(String message, ExpressionContext context, Snippet source) {
        this("Cannot parse the expression: ", message, context, source);
    }

    public PandaExpressionParserFailure(String message, ExpressionContext context, TokenRepresentation source) {
        this(message, context, new PandaSnippet(source));
    }

    public PandaExpressionParserFailure(String message, ExpressionContext context, SourceStream source) {
        this(message, context, source.toSnippet());
    }

    @Override
    public String getLocalizedMessage() {
        return expressionMessage;
    }

    public String getExpressionMessage() {
        return expressionMessage;
    }

}
