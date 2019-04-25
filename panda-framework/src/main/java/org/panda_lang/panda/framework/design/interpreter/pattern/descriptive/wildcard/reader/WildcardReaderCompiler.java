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

package org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.wildcard.reader;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.DescriptivePattern;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;

public class WildcardReaderCompiler {

    private final DescriptivePattern pattern;

    public WildcardReaderCompiler(DescriptivePattern pattern) {
        this.pattern = pattern;
    }

    public @Nullable Object extract(ParserData data, String content, TokenDistributor distributor) {
        for (WildcardReader wildcardReader : pattern.getWildcardReaders()) {
            if (!wildcardReader.match(content)) {
                continue;
            }

            return wildcardReader.read(data, content, distributor);
        }

        return null;
    }

}
