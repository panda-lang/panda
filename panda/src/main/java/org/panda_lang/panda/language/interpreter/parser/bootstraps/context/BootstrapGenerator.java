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

package org.panda_lang.panda.language.interpreter.parser.bootstraps.context;

import org.panda_lang.framework.design.interpreter.parser.ContextParser;
import org.panda_lang.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PandaParserRepresentation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

final class BootstrapGenerator {

    protected <T> ParserRepresentation<ContextParser<T>> generate(BootstrapInitializer<T> initializer, BootstrapContent content) {
        List<BootstrapMethod> methods = initializer.layers.stream()
                                .map(BootstrapMethod::new)
                                .sorted(Comparator.comparingInt(BootstrapMethod::getOrder))
                                .collect(Collectors.toList());

        content.getInterceptor()
                .ifPresent((interceptor -> interceptor.initialize(content)));

        content.getHandler()
                .filter(handler -> handler instanceof BootstrapHandler)
                .map(handler -> (BootstrapHandler) handler)
                .ifPresent(handler -> handler.initialize(content));

        return new PandaParserRepresentation<>(new BootstrapContextParser<>(content, methods), initializer.handler, initializer.priority);
    }

}
