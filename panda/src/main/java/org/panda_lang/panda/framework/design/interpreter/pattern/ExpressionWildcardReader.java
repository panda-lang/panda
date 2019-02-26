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

package org.panda_lang.panda.framework.design.interpreter.pattern;

import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.reader.WildcardReader;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionParserOld;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionSubparsers;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.Arrays;
import java.util.Collection;

class ExpressionWildcardReader implements WildcardReader {

    private final ExpressionParserOld expressionParser;

    public ExpressionWildcardReader(ExpressionParserOld expressionParser) {
        if (expressionParser == null) {
            throw new IllegalArgumentException("ExpressionParser cannot be null");
        }

        this.expressionParser = expressionParser;
    }

    @Override
    public boolean match(String data) {
        return data.startsWith("expression");
    }

    @Override
    public Tokens read(String data, TokenDistributor distributor) {
        String[] datum = StringUtils.splitFirst(data, " ");

        if (ArrayUtils.isEmpty(datum)) {
            Tokens tokens = expressionParser.read(distributor.currentSubSource());

            if (tokens == null) {
                return null;
            }

            distributor.next(tokens.size());
            return tokens;
        }

        String condition = datum[1];
        Collection<String> names = convert(StringUtils.splitFirst(condition, " ")[1]);

        boolean exclude = condition.startsWith("exclude");
        ExpressionSubparsers subparsers;

        if (exclude) {
            subparsers = expressionParser.getSubparsers().fork();
            subparsers.removeSubparsers(names);
        }
        else {
            subparsers = expressionParser.getSubparsers().select(names);
        }

        ExpressionParserOld parser = new ExpressionParserOld(expressionParser, subparsers);
        Tokens source = parser.read(distributor.currentSubSource());

        if (source == null) {
            return null;
        }

        distributor.next(source.size());
        return source;
    }

    private Collection<String> convert(String elements) {
        return Arrays.asList(elements.split(","));
    }

}
