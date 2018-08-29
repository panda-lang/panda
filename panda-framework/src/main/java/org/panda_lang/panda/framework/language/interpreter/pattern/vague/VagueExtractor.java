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

package org.panda_lang.panda.framework.language.interpreter.pattern.vague;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;

public class VagueExtractor {

    private final Separator[] separators;
    private final Token[] dividers;

    public VagueExtractor(Separator[] separators, Token[] dividers) {
        this.separators = separators;
        this.dividers = dividers;
    }

    public VagueResult extract(TokenizedSource source) {
        VagueWorker worker = new VagueWorker(this, new VagueResult(), new PandaTokenReader(source));
        return worker.extract();
    }

    public VagueResult extract(TokenReader reader) {
        VagueWorker worker = new VagueWorker(this, new VagueResult(), reader);
        return worker.extract();
    }

    protected Token[] getDividers() {
        return dividers;
    }

    public Token[] getSeparators() {
        return separators;
    }

}
