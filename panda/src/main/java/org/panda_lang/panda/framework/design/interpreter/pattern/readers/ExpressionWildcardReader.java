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

package org.panda_lang.panda.framework.design.interpreter.pattern.readers;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.DefaultSubparsers;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.reader.WildcardReader;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.Arrays;
import java.util.Collection;

class ExpressionWildcardReader implements WildcardReader {

    private static final ExpressionParser PARSER = new ExpressionParser(DefaultSubparsers.Instances.getDefaultSubparsers());

    @Override
    public boolean match(String data) {
        return data.startsWith("expression");
    }

    @Override
    public Tokens read(String data, TokenDistributor distributor) {
        String[] datum = StringUtils.splitFirst(data, " ");

        if (ArrayUtils.isEmpty(datum)) {
            Tokens tokens = PARSER.read(distributor.currentSubSource());

            if (tokens == null) {
                return null;
            }

            distributor.next(tokens.size());
            return tokens;
        }

        String condition = datum[1];
        Collection<String> names = convert(StringUtils.splitFirst(condition, " ")[1]);

        boolean exclude = condition.startsWith("exclude");
        ExpressionParser parser;

        if (exclude) {
            parser = new ExpressionParser(DefaultSubparsers.Instances.getDefaultSubparsers());
            parser.removeSubparsers(names);
        }
        else {
            parser = new ExpressionParser(DefaultSubparsers.Instances.getDefaultSubparsers(names));
        }

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
