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

import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapContent;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.DescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.PandaDescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;

import java.util.Objects;

public class DescriptivePatternHandler implements BootstrapHandler, ParserHandler {

    private DescriptivePattern pattern;

    @Override
    public void initialize(BootstrapContent content) {
        if (!content.getPattern().isPresent()) {
            return;
        }

        this.pattern = PandaDescriptivePattern.builder()
                .compile(Objects.toString(content.getPattern().get().toString()))
                .build(content.getData());
    }

    @Override
    public Boolean handle(Context context, Channel channel, Snippet source) {
        return pattern.extract(context, source).isMatched();
    }

}
