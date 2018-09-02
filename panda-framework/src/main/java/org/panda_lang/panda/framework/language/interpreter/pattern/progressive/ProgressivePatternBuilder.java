/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.pattern.progressive;

import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.LexicalPattern;

public class ProgressivePatternBuilder {

    protected LexicalPattern<TokenizedSource> pattern;

    protected ProgressivePatternBuilder() { }

    public ProgressivePatternBuilder compile(String pattern) {
        this.pattern = LexicalPattern.<TokenizedSource> builder()
                .compile(pattern)
                .build();

        return this;
    }

    public ProgressivePattern build() {
        return new ProgressivePattern(this);
    }

}
