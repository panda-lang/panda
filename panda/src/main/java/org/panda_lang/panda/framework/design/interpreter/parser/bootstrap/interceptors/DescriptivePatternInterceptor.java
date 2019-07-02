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

package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.interceptors;

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapContent;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInterceptor;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.data.InterceptorData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.pattern.PandaDescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.DescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.DescriptivePatternMapping;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;

public class DescriptivePatternInterceptor implements BootstrapInterceptor {

    private BootstrapContent bootstrap;
    private DescriptivePattern pattern;

    @Override
    public void initialize(BootstrapContent content) {
        this.bootstrap = content;

        if (!content.getPattern().isPresent()) {
            return;
        }

        this.pattern = PandaDescriptivePattern.builder()
                .compile(content.getPattern().get().toString())
                .build(content.getData());
    }

    @Override
    public InterceptorData handle(InterceptorData interceptorData, Context context) {
        if (pattern != null) {
            Snippet currentSource = context.getComponent(UniversalComponents.STREAM).toSnippet();
            ExtractorResult result = pattern.extract(context, context.getComponent(UniversalComponents.STREAM));

            if (!result.isMatched()) {
                PandaFramework.getLogger().error("Bootstrap parser: " + bootstrap.getPattern().orElse("<null pattern>").toString());
                PandaFramework.getLogger().error("Source: " + currentSource.toString());

                throw PandaParserFailure.builder("Interceptor could not match token pattern, error: " + result.getErrorMessage(), context)
                        .withStreamOrigin(currentSource)
                        .withNote("Compare your source with required pattern: " + bootstrap.getPattern().orElse("<null pattern>").toString())
                        .build();
            }

            interceptorData.addElement(new DescriptivePatternMapping(result));
            interceptorData.addElement(result);
        }

        return interceptorData;
    }

}
