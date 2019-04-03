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

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;

public class ExpressionParserException extends PandaParserException {

    private final String expressionMessage;
    private final Snippet source;

    public ExpressionParserException(String prefix, String message, Snippet source) {
        super(prefix + message);
        this.expressionMessage = message;
        this.source = source;
    }

    public ExpressionParserException(String message, Snippet source) {
        this("Cannot parse the expression: ", message, source);
    }

    public ExpressionParserException(String message, TokenRepresentation source) {
        this(message, new PandaSnippet(source));
    }

    public ExpressionParserException(String message, SourceStream source) {
        this(message, source.toSnippet());
    }

    @Override
    public String getLocalizedMessage() {
        return expressionMessage;
    }

    public Snippet getSource() {
        return source;
    }

    public String getExpressionMessage() {
        return expressionMessage;
    }

}
