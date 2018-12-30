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

package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.pattern.AbyssPatternData;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

import java.util.List;

public class AbyssPatternHandler implements BootstrapHandler {

    private AbyssPattern pattern;

    @Override
    public void initialize(PandaParserBootstrap bootstrap) {
        AbyssPatternData data = (AbyssPatternData) bootstrap.getPattern();

        this.pattern = new AbyssPatternBuilder()
                .compile(PandaSyntax.getInstance(), data.getPattern())
                .build();
    }

    @Override
    public boolean handle(ParserData data, SourceStream source) {
        List<Tokens> result = pattern.extractor().extract(source.toTokenReader());
        return result != null && result.size() == pattern.getAmountOfHollows();
    }

}
