/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.interpreter.parser.Contextual;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.language.interpreter.token.PandaSourceStream;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.separator.Separators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ArgumentsParser implements Parser {

    @Override
    public String name() {
        return "arguments";
    }

    public List<Expression> parse(Contextual<?> context, Snippet snippet) {
        if (snippet.isEmpty()) {
            return Collections.emptyList();
        }

        ExpressionParser expressionParser = context.toContext().getExpressionParser();
        List<Expression> expressions = new ArrayList<>((snippet.size() - 1) / 2);
        SourceStream source = new PandaSourceStream(snippet);

        while (source.hasUnreadSource()) {
            Expression expression = expressionParser.parse(context, source);
            expressions.add(expression);

            if (source.hasUnreadSource()) {
                TokenInfo comma = source.read();

                if (!Separators.COMMA.equals(comma.getToken())) {
                    throw new PandaParserFailure(snippet, comma, "Illegal token", "Remove highlighted comma");
                }

                if (!source.hasUnreadSource()) {
                    throw new PandaParserFailure(source, comma, "Arguments cannot end with a comma", "Remove the comma at the end of arguments");
                }
            }
        }

        return expressions;
    }

}
