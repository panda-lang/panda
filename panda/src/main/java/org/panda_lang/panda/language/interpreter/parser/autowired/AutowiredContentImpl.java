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

import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.pipeline.Handler;

import org.panda_lang.utilities.commons.function.Option;

final class AutowiredContentImpl<P> implements AutowiredContent<P> {

    private final String name;
    private final Object instance;
    private final Context context;
    private final Handler handler;
    private final IterationInitializer<P> interceptor;
    private final P pattern;

    AutowiredContentImpl(String name, Object instance, Context context, Handler handler, IterationInitializer<P> interceptor, P pattern) {
        this.name = name;
        this.instance = instance;
        this.context = context;
        this.handler = handler;
        this.interceptor = interceptor;
        this.pattern = pattern;
    }

    @Override
    public IterationInitializer<P> getInitializer() {
        return interceptor;
    }

    @Override
    public Option<Handler> getHandler() {
        return Option.of(handler);
    }

    @Override
    public Option<P> getPattern() {
        return Option.of(pattern);
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Object getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return name;
    }

}
