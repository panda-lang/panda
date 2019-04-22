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

package org.panda_lang.panda.framework.language.resource.parsers.general;

import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.extractor.GappedPatternExtractor;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParser implements Parser {

    private static final GappedPattern PATTERN = new GappedPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+* , +*")
            .build();

    public Expression[] parse(ParserData data, Snippet snippet) {
        if (snippet.isEmpty()) {
            return new Expression[0];
        }

        SourceStream sourceStream = new PandaSourceStream(snippet);
        List<Expression> expressions = new ArrayList<>();
        ExpressionParser expressionParser = data.getComponent(UniversalComponents.EXPRESSION);
        GappedPatternExtractor extractor = PATTERN.extractor();

        while (sourceStream.hasUnreadSource()) {
            TokenReader reader = sourceStream.toTokenReader();
            List<Snippet> gaps = extractor.extract(reader);

            if (gaps == null) {
                Expression expression = readArgument(data, expressionParser, sourceStream.toSnippet());
                expressions.add(expression);
                break;
            }

            Snippet argument = gaps.get(0);
            Expression expression = readArgument(data, expressionParser, argument);

            expressions.add(expression);
            sourceStream.read(argument.size() + 1);
        }

        Expression[] expressionsArray = new Expression[expressions.size()];
        expressions.toArray(expressionsArray);

        return expressionsArray;
    }

    private Expression readArgument(ParserData data, ExpressionParser expressionParser, Snippet argument) {
        Expression expression = expressionParser.parse(data, argument);

        if (expression == null) {
            throw new PandaParserFailure("Cannot parse argument " + argument, data);
        }

        return expression;
    }

}
