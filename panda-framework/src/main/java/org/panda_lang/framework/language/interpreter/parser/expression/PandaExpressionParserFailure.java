/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.parser.expression;

import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.source.IndicatedSource;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;

public final class PandaExpressionParserFailure extends PandaParserFailure {

    private final String expressionMessage;

    public PandaExpressionParserFailure(ExpressionContext context, Snippetable indicated, String message, String note) {
        super(context.getContext(), indicated, message, note);
        this.expressionMessage = message;
    }

    public PandaExpressionParserFailure(Throwable cause, ExpressionContext context, IndicatedSource indicatedSource, String message) {
        super(cause, context.getContext(), indicatedSource, message, null);
        this.expressionMessage = message;
    }

    public PandaExpressionParserFailure(ExpressionContext context, Snippetable indicated, String message) {
        this(context, indicated, message, null);
    }

    @Override
    public String getMessage() {
        return expressionMessage;
    }

    @Override
    public String getLocalizedMessage() {
        return super.getMessage();
    }

}
