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
import org.panda_lang.panda.framework.language.interpreter.token.distributors.DiffusedSource;
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
        return read(context, context.getDiffusedSource());
    }

    public @Nullable ExpressionResult<Expression> read(ExpressionContext context, DiffusedSource source) {
        if (!type.equals(source.getCurrent().getToken())) {
            return null;
        }

        SeparatorStack separators = new SeparatorStack();
        separators.check(source.getCurrent().getToken());

        this.content = new PandaSnippet();
        source.backup();

        for (TokenRepresentation next : source) {
            boolean separator = separators.check(next.getToken());

            if (separator && !separators.isLocked() && next.contentEquals(type.getOpposite())) {
                return contentProcessor.process(this, context, content, next);
            }

            content.addToken(next);
        }

        source.restore();
        this.content = null;
        return null;
    }

    public boolean hasContent() {
        return getContent() != null;
    }

    public Snippet getContent() {
        return content;
    }

}
