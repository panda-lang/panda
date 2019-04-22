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

package org.panda_lang.panda.framework.language.interpreter.parser.expression.util;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

public interface ContentProcessor {

    /**
     * The default implementation of processor which parse the content as an expression
     */
    ContentProcessor DEFAULT = (reader, context, content, lastToken) -> {
        if (content.isEmpty()) {
            return ExpressionResult.error("Expression expected", lastToken);
        }

        return ExpressionResult.of(context.getParser().parse(context.getData(), content));
    };

    /**
     * The stub implementation of processor which always returns null as a result
     */
    ContentProcessor NON_PROCESSING = (reader, context, content, last) -> null;


    /**
     *
     * Process the read content and prepare the result
     *
     * @param reader the current content reader instance
     * @param context the expression context
     * @param content the content between separators
     * @param last the last token, it is the closing separator (useful for errors, if the content is empty)
     * @return the processed result
     */
    @Nullable ExpressionResult<Expression> process(SeparatedContentReader reader, ExpressionContext context, Snippet content, TokenRepresentation last);

}
