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

package org.panda_lang.panda.framework.design.interpreter.pattern.utils;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorWorker;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.wildcard.reader.WildcardReader;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParserSettings;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.Arrays;
import java.util.Collection;

public class ExpressionWildcardReader implements WildcardReader<Expression> {

    public static long time;
    private final ExpressionParser expressionParser;

    public ExpressionWildcardReader(ExpressionParser expressionParser) {
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
    public @Nullable Expression read(ParserData data, String content, TokenDistributor distributor) {
        String[] datum = StringUtils.splitFirst(content, " ");

        if (ArrayUtils.isEmpty(datum)) {
            return parse(expressionParser, null, data, distributor, distributor.currentSubSource());
        }

        String condition = datum[1];
        Collection<String> names = convert(StringUtils.splitFirst(condition, " ")[1]);
        long uptime = System.nanoTime();

        ExpressionParserSettings settings = ExpressionParserSettings.create()
                .withSelectedSubparsers(names);
        Expression expression = parse(expressionParser, condition.startsWith("exclude") ? settings.excludeSelected() : settings.includeSelected(), data, distributor, distributor.currentSubSource());

        uptime = System.nanoTime() - uptime;
        ExtractorWorker.fullTime -= uptime;
        time += uptime;

        return expression;
    }

    private @Nullable Expression parse(ExpressionParser expressionParser, @Nullable ExpressionParserSettings settings, ParserData data, TokenDistributor distributor, Snippet content) {
        SourceStream source = new PandaSourceStream(content);
        Expression expression = settings == null ? expressionParser.parse(data, source) : expressionParser.parse(data, source, settings);

        distributor.next(source.getReadLength());
        return expression;
    }

    private Collection<String> convert(String elements) {
        return Arrays.asList(StringUtils.split(elements, ","));
    }

}
