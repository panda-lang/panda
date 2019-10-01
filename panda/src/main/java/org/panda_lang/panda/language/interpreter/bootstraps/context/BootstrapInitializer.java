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

package org.panda_lang.panda.language.interpreter.bootstraps.context;

import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.ContextParser;
import org.panda_lang.framework.design.interpreter.parser.ParserRepresentation;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Handler;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Autowired;
import org.panda_lang.utilities.commons.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

public class BootstrapInitializer<T> {

    protected String name;
    protected Object instance;

    protected String pipeline;
    protected Handler handler;
    protected int priority;

    protected Object pattern;
    protected BootstrapInterceptor interceptor;
    protected final Collection<Method> layers = new ArrayList<>();

    BootstrapInitializer() { }

    public BootstrapInitializer<T> instance(Object object) {
        this.instance = object;
        return this;
    }

    public BootstrapInitializer<T> name(String name) {
        this.name = name;
        return this;
    }

    public BootstrapInitializer<T> pipeline(String pipeline) {
        this.pipeline = pipeline;
        return this;
    }

    public BootstrapInitializer<T> interceptor(BootstrapInterceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    public BootstrapInitializer<T> handler(Handler handler) {
        this.handler = handler;
        return this;
    }

    public BootstrapInitializer<T> pattern(Object pattern) {
        this.pattern = pattern;
        return this;
    }

    public BootstrapInitializer<T> priority(int priority) {
        this.priority = priority;
        return this;
    }

    public BootstrapInitializer<T> layers(Class<?> clazz) {
        return layers(ReflectionUtils.getMethodsAnnotatedWith(clazz, Autowired.class));
    }

    public BootstrapInitializer<T> layers(Collection<Method> methods) {
        layers.addAll(methods);
        return this;
    }

    protected ParserRepresentation<ContextParser<T>> build(Context context) {
        if (name == null && instance != null) {
            name(instance.getClass().getSimpleName());
        }

        if (layers.isEmpty() && instance != null) {
            layers(instance.getClass());
        }

        if (layers.isEmpty()) {
            throw new BootstrapException("Bootstrap does not contain any layers");
        }

        return new BootstrapGenerator().generate(this, new BootstrapContentImpl(name, instance, context, handler, interceptor, pattern));
    }

}
