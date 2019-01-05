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

package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.interceptor;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedBootstrapParser;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.design.interpreter.pattern.AbyssPatternData;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.mapping.PatternMapping;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.utils.AbyssPatternAssistant;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;
import org.panda_lang.panda.utilities.commons.StringUtils;

public class AbyssPatternInterceptor implements BootstrapInterceptor {

    private AbyssPatternData patternData;
    private AbyssPattern pattern;

    @Override
    public void initialize(PandaParserBootstrap generator, ParserData data) {
        this.patternData = (AbyssPatternData) generator.getPattern();

        if (StringUtils.isEmpty(patternData.getPattern())) {
            return;
        }

        this.pattern = new AbyssPatternBuilder()
                .compile(PandaSyntax.getInstance(), patternData.getPattern())
                .lastIndexAlgorithm(patternData.getLastIndexAlgorithm())
                .maxNestingLevel(patternData.getMaxNestingLevel())
                .build();
    }

    @Override
    public InterceptorData handle(UnifiedBootstrapParser parser, ParserData data) {
        InterceptorData interceptorData = new InterceptorData();

        if (pattern != null) {
            PatternMapping patternMapping = AbyssPatternAssistant.traditionalMapping(pattern, data, patternData.getWildcards());

            if (patternMapping == null) {
                throw new PandaParserFailure("Handler error", data);
            }

            interceptorData.addElement(patternMapping);
        }

        return interceptorData;
    }

}
