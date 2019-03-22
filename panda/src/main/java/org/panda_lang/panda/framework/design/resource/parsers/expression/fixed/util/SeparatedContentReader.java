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
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionContext;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionResult;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;
import org.panda_lang.panda.framework.language.resource.syntax.separator.SeparatorStack;

public class SeparatedContentReader {

    private final Separator type;
    private final ContentProcessor contentProcessor;
    private Snippet content;

    public SeparatedContentReader(Separator type, ContentProcessor contentProcessor) {
        if (!type.hasOpposite()) {
            throw new IllegalArgumentException("Reader requires separator with an opposite token");
        }

        this.type = type;
        this.contentProcessor = contentProcessor;
    }

    public @Nullable ExpressionResult<Expression> read(ExpressionContext context) {
        if (!type.equals(context.getNext().getToken())) {
            return null;
        }

        SeparatorStack separators = new SeparatorStack();
        separators.check(context.getNext().getToken());

        this.content = new PandaSnippet();
        int cachedIndex = context.getReader().getIndex();

        for (TokenRepresentation next : context.getReader()) {
            boolean result = separators.check(next.getToken());

            if (!result && !separators.isLocked() && next.contentEquals(type.getOpposite())) {
                return contentProcessor.process(this, context, content, next);
            }

            content.addToken(next);
        }

        return null;
    }

    public Snippet getContent() {
        return content;
    }

}
