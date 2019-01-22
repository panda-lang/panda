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

package org.panda_lang.panda.framework.design.interpreter.pattern.progressive;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;

public class ProgressivePattern {

    private final Separator[] separators;
    private final Token[] dividers;

    public ProgressivePattern(Separator[] separators, Token[] dividers) {
        this.separators = separators;
        this.dividers = dividers;
    }

    public ProgressivePatternResult extract(Tokens source) {
        ProgressivePatternWorker worker = new ProgressivePatternWorker(this, new ProgressivePatternResult(), new PandaTokenReader(source));
        return worker.extract();
    }

    public ProgressivePatternResult extract(TokenReader reader) {
        ProgressivePatternWorker worker = new ProgressivePatternWorker(this, new ProgressivePatternResult(), reader);
        return worker.extract();
    }

    protected Token[] getDividers() {
        return dividers;
    }

    public Token[] getSeparators() {
        return separators;
    }

}
