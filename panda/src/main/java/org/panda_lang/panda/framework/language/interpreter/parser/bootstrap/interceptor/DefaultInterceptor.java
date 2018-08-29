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

package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.interceptor;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.UnifiedBootstrapParser;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternAssistant;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;
import org.panda_lang.panda.utilities.commons.objects.StringUtils;

public class DefaultInterceptor implements BootstrapInterceptor {

    private final boolean lastIndexAlgorithm;
    private AbyssPattern pattern;
    private String[] wildcards;

    public DefaultInterceptor(boolean lastIndexAlgorithm) {
        this.lastIndexAlgorithm = lastIndexAlgorithm;
    }

    public DefaultInterceptor() {
        this(false);
    }

    @Override
    public void initialize(PandaParserBootstrap generator) {
        this.wildcards = generator.getWildcards();

        if (StringUtils.isEmpty(generator.getPattern())) {
            return;
        }

        this.pattern = new AbyssPatternBuilder()
                .compile(PandaSyntax.getInstance(), generator.getPattern())
                .lastIndexAlgorithm(lastIndexAlgorithm)
                .build();
    }

    @Override
    public InterceptorData handle(UnifiedBootstrapParser parser, ParserData data) {
        InterceptorData interceptorData = new InterceptorData();

        if (pattern != null) {
            AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(pattern, data, wildcards);
            interceptorData.addElement(redactor);
        }

        return interceptorData;
    }

}
