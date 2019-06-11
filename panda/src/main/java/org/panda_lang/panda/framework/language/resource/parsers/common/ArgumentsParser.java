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

package org.panda_lang.panda.framework.language.resource.parsers.common;

import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

import java.util.ArrayList;
import java.util.List;

public class ArgumentsParser implements Parser {

    private static final Expression[] EMPTY = new Expression[0];

    public Expression[] parse(ParserData data, Snippet snippet) {
        if (snippet.isEmpty()) {
            return EMPTY;
        }

        ExpressionParser parser = data.getComponent(UniversalComponents.EXPRESSION);
        List<Expression> expressions = new ArrayList<>((snippet.size() - 1) / 2);
        SourceStream source = new PandaSourceStream(snippet);

        while (source.hasUnreadSource()) {
            Expression expression = parser.parse(data, source);
            expressions.add(expression);

            if (source.hasUnreadSource()) {
                TokenRepresentation comma = source.read();

                if (!Separators.COMMA.equals(comma.getToken())) {
                    throw PandaParserFailure.builder("Illegal token", data)
                            .withSource(snippet, comma)
                            .withNote("Remove highlighted comma")
                            .build();
                }

                if (!source.hasUnreadSource()) {
                    throw PandaParserFailure.builder("Arguments cannot end with a comma", data)
                            .withSource(source, comma)
                            .build();
                }
            }
        }

        return expressions.toArray(EMPTY);
    }

}
