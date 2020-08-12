/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.interpreter.parser.context;

import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.ParserRepresentation;
import org.panda_lang.language.interpreter.parser.pipeline.PandaParserRepresentation;
import org.panda_lang.utilities.inject.DependencyInjection;
import org.panda_lang.utilities.inject.Injector;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

final class BootstrapGenerator {

    private static final Injector INJECTOR = DependencyInjection.createInjector(new BootstrapInjectorController());

    protected <T> ParserRepresentation<ContextParser<T>> generate(BootstrapInitializer<T> initializer, BootstrapContent content) {
        List<BootstrapMethod> methods = new ArrayList<>(initializer.layers.size());

        for (Method layer : initializer.layers) {
            try {
                methods.add(new BootstrapMethod(INJECTOR.forGeneratedMethod(layer)));
            } catch (Exception e) {
                e.printStackTrace();
                throw new BootstrapException("Cannot generate bootstrap method", e);
            }
        }

        methods.sort(Comparator.comparingInt(BootstrapMethod::getOrder));
        content.getInitializer().initialize(content);

        content.getHandler()
                .filter(handler -> handler instanceof BootstrapHandler)
                .map(handler -> (BootstrapHandler) handler)
                .peek(handler -> handler.initialize(content));

        return new PandaParserRepresentation<>(new BootstrapContextParser<>(content, methods), initializer.handler, initializer.priority);
    }

}
