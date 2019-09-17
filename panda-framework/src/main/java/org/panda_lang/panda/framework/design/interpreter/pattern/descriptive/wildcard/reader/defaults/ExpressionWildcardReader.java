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

package org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.wildcard.reader.defaults;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParserSettings;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.wildcard.reader.WildcardReader;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.utils.TokenDistributor;
import org.panda_lang.panda.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.Arrays;
import java.util.Collection;

public class ExpressionWildcardReader implements WildcardReader<Expression> {

    private final ExpressionParser expressionParser;

    public ExpressionWildcardReader(ExpressionParser expressionParser) {
        if (expressionParser == null) {
            throw new IllegalArgumentException("ExpressionParser cannot be null");
        }

        this.expressionParser = expressionParser;
    }

    @Override
    public boolean match(String context) {
        return context.startsWith("expression");
    }

    @Override
    public @Nullable Expression read(Context context, String content, TokenDistributor distributor) {
        String[] datum = StringUtils.splitFirst(content, " ");

        if (ArrayUtils.isEmpty(datum)) {
            return parse(expressionParser, null, context, distributor, distributor.currentSubSource());
        }

        String condition = datum[1];
        Collection<String> names = convert(StringUtils.splitFirst(condition, " ")[1]);
        ExpressionParserSettings settings = ExpressionParserSettings.create().withSelectedSubparsers(names);

        return parse(expressionParser, condition.startsWith("exclude") ? settings.excludeSelected() : settings.includeSelected(), context, distributor, distributor.currentSubSource());
    }

    private @Nullable Expression parse(ExpressionParser expressionParser, @Nullable ExpressionParserSettings settings, Context context, TokenDistributor distributor, Snippet content) {
        SourceStream source = new PandaSourceStream(content);
        Expression expression = settings == null ? expressionParser.parse(context, source) : expressionParser.parse(context, source, settings);

        distributor.next(source.getReadLength());
        return expression;
    }

    private Collection<String> convert(String elements) {
        return Arrays.asList(StringUtils.split(elements, ","));
    }

}
