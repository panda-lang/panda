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

package org.panda_lang.panda.language.interpreter.parser.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;

import java.util.ArrayList;
import java.util.List;

public final class ArgumentsParser implements Parser {

    private static final Expression[] EMPTY = new Expression[0];

    public Expression[] parse(Context context, Snippet snippet) {
        return parse(context, null, snippet);
    }

    public Expression[] parse(ExpressionContext expressionContext, Snippet snippet) {
        return parse(expressionContext.getContext(), expressionContext, snippet);
    }

    private Expression[] parse(Context context, @Nullable ExpressionContext expressionContext, Snippet snippet) {
        if (snippet.isEmpty()) {
            return EMPTY;
        }

        ExpressionParser expressionParser = context.getComponent(Components.EXPRESSION);
        List<Expression> expressions = new ArrayList<>((snippet.size() - 1) / 2);
        SourceStream source = new PandaSourceStream(snippet);

        while (source.hasUnreadSource()) {
            ExpressionTransaction transaction = expressionParser.parse(context, source);

            if (expressionContext != null) {
                expressionContext.commit(transaction::rollback);
            }

            expressions.add(transaction.getExpression());

            if (source.hasUnreadSource()) {
                TokenRepresentation comma = source.read();

                if (!Separators.COMMA.equals(comma.getToken())) {
                    throw new PandaParserFailure(context, snippet, comma, "Illegal token", "Remove highlighted comma");
                }

                if (!source.hasUnreadSource()) {
                    throw new PandaParserFailure(context, source, comma, "Arguments cannot end with a comma", "Remove the comma at the end of arguments");
                }
            }
        }

        return expressions.toArray(EMPTY);
    }

}
