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

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapCoreParser;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.pattern.PandaDescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.DescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.DescriptivePatternMapping;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;

public class DescriptivePatternInterceptor implements BootstrapInterceptor {

    private PandaParserBootstrap bootstrap;
    private DescriptivePattern pattern;

    @Override
    public void initialize(PandaParserBootstrap bootstrap, ParserData data) {
        this.bootstrap = bootstrap;

        if (bootstrap.getPattern() == null) {
            return;
        }

        this.pattern = PandaDescriptivePattern.builder()
                .compile(bootstrap.getPattern().toString())
                .build(data);
    }

    @Override
    public InterceptorData handle(BootstrapCoreParser parser, ParserData data) {
        InterceptorData interceptorData = new InterceptorData();

        if (pattern != null) {
            Snippet currentSource = data.getComponent(UniversalComponents.SOURCE_STREAM).toSnippet();
            ExtractorResult result = pattern.extract(data, data.getComponent(UniversalComponents.SOURCE_STREAM));

            if (!result.isMatched()) {
                PandaFramework.getLogger().error("Bootstrap parser: " + bootstrap.getPattern().toString());
                PandaFramework.getLogger().error("Source: " + currentSource.toString());

                throw new PandaParserFailure("Interceptor could not match token pattern, error: " + result.getErrorMessage(), data, currentSource);
            }

            interceptorData.addElement(new DescriptivePatternMapping(result));
            interceptorData.addElement(result);
        }

        return interceptorData;
    }

}
