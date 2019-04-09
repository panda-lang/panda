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
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapCoreParser;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.pattern.PandaTokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPatternMapping;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;

public class TokenPatternInterceptor implements BootstrapInterceptor {

    private TokenPattern pattern;

    @Override
    public void initialize(PandaParserBootstrap bootstrap, ParserData data) {
        if (bootstrap.getPattern() == null) {
            return;
        }

        this.pattern = PandaTokenPattern.builder()
                .compile(bootstrap.getPattern().toString())
                .build(data);
    }

    @Override
    public InterceptorData handle(BootstrapCoreParser parser, ParserData data) {
        InterceptorData interceptorData = new InterceptorData();

        if (pattern != null) {
            ExtractorResult result = pattern.extract(data, data.getComponent(UniversalComponents.SOURCE_STREAM));

            if (!result.isMatched()) {
                data.getComponent(UniversalComponents.SOURCE_STREAM).updateCachedSource();
                throw new PandaParserFailure("Interceptor could not match token pattern, error: " + result.getErrorMessage(), data);
            }

            interceptorData.addElement(new TokenPatternMapping(result));
            interceptorData.addElement(result);
        }

        return interceptorData;
    }

}
