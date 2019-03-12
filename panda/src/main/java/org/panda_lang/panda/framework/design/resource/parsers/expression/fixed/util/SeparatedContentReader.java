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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionResult;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;
import org.panda_lang.panda.framework.language.resource.syntax.separator.SeparatorStack;

public class SeparatedContentReader {

    private final Separator type;
    private final ContentProcessor contentProcessor;

    private SeparatorStack separators;
    private Tokens content;

    public SeparatedContentReader(Separator type, ContentProcessor contentProcessor) {
        if (!type.hasOpposite()) {
            throw new IllegalArgumentException("Reader requires separator with an opposite token");
        }

        this.type = type;
        this.contentProcessor = contentProcessor;
    }

    public @Nullable ExpressionResult<Expression> read(ExpressionParser parser, ParserData data, TokenRepresentation token) {
        if (isDone()) {
            return null;
        }

        // initialize
        if (content == null && separators == null) {
            if (!token.contentEquals(type)) {
                return null;
            }

            this.separators = new SeparatorStack();
            this.content = new PandaTokens();
            return ExpressionResult.empty();
        }

        // not initialized
        if (content == null) {
            return null;
        }

        // exclude separators not related to the section
        boolean result = separators.check(token.getToken());

        if (!result && !separators.isLocked() && token.contentEquals(type.getOpposite())) {
            separators = null;
            return contentProcessor.process(this, parser, data, content, token);
        }

        content.addToken(token);
        return ExpressionResult.empty();
    }

    public Tokens getContent() {
        return content;
    }

    public boolean isDone() {
        return content != null && separators == null;
    }

    public interface ContentProcessor {

        ContentProcessor DEFAULT = (reader, parser, data, content, lastToken) -> {
            if (content.isEmpty()) {
                return ExpressionResult.error("Expression expected", lastToken);
            }

            return ExpressionResult.of(parser.parse(data, content));
        };

        @Nullable ExpressionResult<Expression> process(SeparatedContentReader reader, ExpressionParser parser, ParserData data, Tokens content, TokenRepresentation last);

    }

}
