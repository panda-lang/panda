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

package org.panda_lang.panda.language.resource.head;

import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.resource.syntax.separator.Separators;

import java.util.ArrayList;
import java.util.List;

public class ArgumentsParser implements Parser {

    private static final Expression[] EMPTY = new Expression[0];

    public Expression[] parse(Context context, Snippet snippet) {
        if (snippet.isEmpty()) {
            return EMPTY;
        }

        ExpressionParser expressionParser = context.getComponent(UniversalComponents.EXPRESSION);
        List<Expression> expressions = new ArrayList<>((snippet.size() - 1) / 2);
        SourceStream source = new PandaSourceStream(snippet);

        while (source.hasUnreadSource()) {
            Expression expression = expressionParser.parse(context, source);
            expressions.add(expression);

            if (source.hasUnreadSource()) {
                TokenRepresentation comma = source.read();

                if (!Separators.COMMA.equals(comma.getToken())) {
                    throw PandaParserFailure.builder("Illegal token", context)
                            .withSource(snippet, comma)
                            .withNote("Remove highlighted comma")
                            .build();
                }

                if (!source.hasUnreadSource()) {
                    throw PandaParserFailure.builder("Arguments cannot end with a comma", context)
                            .withSource(source, comma)
                            .build();
                }
            }
        }

        return expressions.toArray(EMPTY);
    }

}
