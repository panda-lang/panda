/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.syntax.expressions.subparsers;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.parser.Contextual;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.Parser;
import panda.interpreter.parser.expression.ExpressionParser;
import panda.interpreter.token.PandaSourceStream;
import panda.interpreter.token.Snippet;
import panda.interpreter.token.SourceStream;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.resource.syntax.separator.Separators;

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
