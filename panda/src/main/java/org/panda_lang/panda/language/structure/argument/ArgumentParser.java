/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.structure.argument;

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.framework.implementation.parser.PandaParserException;
import org.panda_lang.panda.framework.implementation.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.Parser;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.extractor.Extractor;
import org.panda_lang.panda.framework.language.interpreter.token.reader.TokenReader;
import org.panda_lang.panda.language.structure.expression.Expression;
import org.panda_lang.panda.language.structure.expression.ExpressionParser;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParser implements Parser {

    private static final TokenPattern PATTERN = TokenPattern.builder()
            .hollow()
            .unit(TokenType.SEPARATOR, ",")
            .hollow()
            .build();

    public Expression[] parse(ParserInfo info, TokenizedSource tokenizedSource) {
        SourceStream sourceStream = new PandaSourceStream(tokenizedSource);

        List<Expression> expressions = new ArrayList<>();
        ExpressionParser expressionParser = new ExpressionParser();
        Extractor extractor = PATTERN.extractor();

        while (sourceStream.hasUnreadSource()) {
            TokenReader reader = sourceStream.toTokenReader();
            List<TokenizedSource> gaps = extractor.extract(reader);

            if (gaps == null) {
                Expression expression = readArgument(info, expressionParser, sourceStream.toTokenizedSource());
                expressions.add(expression);
                break;
            }

            TokenizedSource argument = gaps.get(0);
            Expression expression = readArgument(info, expressionParser, argument);

            expressions.add(expression);
            sourceStream.read(argument.size() + 1);
        }

        Expression[] expressionsArray = new Expression[expressions.size()];
        expressions.toArray(expressionsArray);

        return expressionsArray;
    }

    private Expression readArgument(ParserInfo info, ExpressionParser expressionParser, TokenizedSource argument) {
        Expression expression = expressionParser.parse(info, argument);

        if (expression == null) {
            throw new PandaParserException("Cannot parse argument " + argument + " at line " + (argument.get(0).getLine() + 1));
        }

        return expression;
    }

}
