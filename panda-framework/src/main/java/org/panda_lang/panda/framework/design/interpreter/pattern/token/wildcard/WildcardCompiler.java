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

package org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.condition.WildcardConditionCompiler;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.reader.WildcardReaderCompiler;
import org.panda_lang.panda.utilities.commons.StringUtils;

public class WildcardCompiler {

    private final TokenPattern pattern;
    private final WildcardConditionCompiler conditionCompiler;
    private final WildcardReaderCompiler readerCompiler;

    public WildcardCompiler(TokenPattern pattern) {
        this.pattern = pattern;
        this.conditionCompiler = new WildcardConditionCompiler(pattern);
        this.readerCompiler = new WildcardReaderCompiler(pattern);
    }

    public @Nullable Snippet compile(String data, TokenDistributor distributor) {
        String[] entry = StringUtils.splitFirst(data, " ");
        String type = entry[0];
        String value = entry[1];

        if (type.equals("condition")) {
            return conditionCompiler.extract(value, distributor);
        }

        if (type.equals("reader")) {
            return readerCompiler.extract(value, distributor);
        }

        throw new TokenPatternWildcardException("Unknown condition: " + type);
    }

}
