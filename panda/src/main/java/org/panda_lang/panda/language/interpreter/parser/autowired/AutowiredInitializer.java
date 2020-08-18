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

package org.panda_lang.panda.language.interpreter.parser.autowired;

import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.ParserRepresentation;
import org.panda_lang.language.interpreter.parser.pipeline.Handler;
import org.panda_lang.language.interpreter.parser.pipeline.PandaParserRepresentation;
import org.panda_lang.language.interpreter.pattern.functional.FunctionalPattern;
import org.panda_lang.language.interpreter.pattern.functional.FunctionalPatternBuilder;
import org.panda_lang.language.interpreter.pattern.linear.LinearPattern;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.autowired.handlers.FunctionalPatternHandler;
import org.panda_lang.panda.language.interpreter.parser.autowired.handlers.LinearPatternHandler;
import org.panda_lang.panda.language.interpreter.parser.autowired.initializers.FunctionalPatternInitializer;
import org.panda_lang.panda.language.interpreter.parser.autowired.initializers.LinearPatternInitializer;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.ReflectionUtils;
import org.panda_lang.utilities.inject.DependencyInjection;
import org.panda_lang.utilities.inject.Injector;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class AutowiredInitializer<T> {

    private static final Injector INJECTOR = DependencyInjection.createInjector(new AutowiredInjectorController());

    protected String parserName;
    protected Object parserInstance;
    protected int priority;
    protected Handler handler;
    protected Object pattern;
    protected IterationInitializer<?> initializer;
    protected final Collection<Method> stages = new ArrayList<>();

    AutowiredInitializer() { }

    public AutowiredInitializer<T> instance(Object object) {
        this.parserInstance = object;
        return this;
    }

    public AutowiredInitializer<T> name(String name) {
        this.parserName = name;
        return this;
    }

    public AutowiredInitializer<T> initializer(IterationInitializer<?> initializer) {
        this.initializer = initializer;
        return this;
    }

    public AutowiredInitializer<T> handler(Handler handler) {
        this.handler = handler;
        return this;
    }

    public AutowiredInitializer<T> pattern(Object pattern) {
        this.pattern = pattern;
        return this;
    }

    public AutowiredInitializer<T> linear(String pattern) {
        LinearPattern linearPattern = LinearPattern.compile(pattern);

        if (handler == null) {
            handler(new LinearPatternHandler(linearPattern));
        }

        initializer(new LinearPatternInitializer(linearPattern));
        this.pattern = linearPattern;
        return this;
    }

    public AutowiredInitializer<T> functional(Function<FunctionalPatternBuilder<?, ?>, FunctionalPatternBuilder<?, ?>> function) {
        FunctionalPattern functionalPattern = function.apply(FunctionalPattern.builder()).build();

        if (handler == null) {
            handler(new FunctionalPatternHandler(functionalPattern));
        }

        initializer(new FunctionalPatternInitializer(functionalPattern));
        this.pattern = functionalPattern;
        return this;
    }

    public AutowiredInitializer<T> priority(int priority) {
        this.priority = priority;
        return this;
    }

    public AutowiredInitializer<T> stages(Class<?> clazz) {
        return stages(ReflectionUtils.getMethodsAnnotatedWith(clazz, Autowired.class));
    }

    public AutowiredInitializer<T> stages(Collection<Method> methods) {
        stages.addAll(methods);
        return this;
    }

    protected ParserRepresentation<ContextParser<T>> build() {
        if (parserName == null && parserInstance != null) {
            name(parserInstance.getClass().getSimpleName());
        }

        if (stages.isEmpty() && parserInstance != null) {
            stages(parserInstance.getClass());
        }

        if (stages.isEmpty()) {
            throw new AutowiredException("AutowiredParser does not contain any layers");
        }

        if (initializer == null) {
            initializer = (ctx, channel) -> {};
        }

        AutowiredContent<?> content = new AutowiredContentImpl<>(parserInstance, handler, ObjectUtils.cast(initializer), pattern);
        List<AutowiredMethod> methods = new ArrayList<>(stages.size());

        for (Method autowiredMethod : stages) {
            try {
                methods.add(new AutowiredMethod(INJECTOR.forGeneratedMethod(autowiredMethod)));
            } catch (Exception exception) {
                throw new AutowiredException("Cannot generate autowired method " + autowiredMethod, exception);
            }
        }

        methods.sort(Comparator.comparingInt(AutowiredMethod::getOrder));
        content.getInitializer().initialize();

        return new PandaParserRepresentation<>(new AutowiredContextParser<>(content, methods), handler, priority);
    }

}
