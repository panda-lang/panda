/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.utilities.inject;

import io.vavr.control.Option;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.inject.annotations.Injectable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

final class InjectorProcessor {

    protected static final Object[] EMPTY = { };

    private final Injector injector;
    private final Map<Executable, Annotation[]> injectableCache = new HashMap<>();

    InjectorProcessor(Injector injector) {
        this.injector = injector;
    }

    protected Object[] fetchValues(InjectorCache cache, Executable executable) throws InjectorException {
        Parameter[] parameters = executable.getParameters();
        Object[] values = new Object[cache.getAnnotations().length];

        for (int index = 0; index < values.length; index++) {
            values[index] = tryFetchValue(cache, parameters[index], index);
        }

        return values;
    }

    protected Object tryFetchValue(InjectorProcessor processor, Parameter parameter) throws InjectorException {
        InjectorCache cache = InjectorCache.of(processor, parameter.getDeclaringExecutable());
        return tryFetchValue(cache, parameter, ArrayUtils.indexOf(parameter.getDeclaringExecutable().getParameters(), parameter));
    }

    protected Annotation[] fetchAnnotations(Executable executable) {
        Annotation[] injectorAnnotations = injectableCache.get(executable);

        if (injectorAnnotations != null) {
            return injectorAnnotations;
        }

        injectorAnnotations = new Annotation[executable.getParameterTypes().length];
        Annotation[][] parameterAnnotations = injector.getResources().fetchAnnotations(executable);

        for (int index = 0; index < parameterAnnotations.length; index++) {
            for (Annotation annotation : parameterAnnotations[index]) {
                if (annotation.annotationType().isAnnotationPresent(Injectable.class)) {
                    injectorAnnotations[index] = annotation;
                }
            }
        }

        injectableCache.put(executable, injectorAnnotations);
        return injectorAnnotations;
    }

    protected InjectorResourceBind<?, ? super Object>[] fetchBinds(Annotation[] annotations, Executable executable) {
        InjectorResources resources = injector.getResources();
        Parameter[] parameters = executable.getParameters();
        InjectorResourceBind<?, ? super Object>[] binds = ObjectUtils.cast(new InjectorResourceBind[parameters.length]);

        for (int index = 0; index < annotations.length; index++) {
            Annotation annotation = annotations[index];
            Parameter parameter = parameters[index];

            Class<?> requiredType = annotation != null ? annotation.annotationType() : parameter.getType();
            Option<InjectorResourceBind<?, ? super Object>> bindValue = resources.getBind(requiredType);

            binds[index] = bindValue.getOrElseThrow(() -> {
                throw new InjectorException("Missing type bind for " + parameter + " parameter");
            });
        }

        return binds;
    }

    protected Collection<InjectorResourceHandler<?, ? >>[] fetchHandlers(Executable executable) {
        Collection<InjectorResourceHandler<?, ? >>[] handlers = ObjectUtils.cast(new Collection[executable.getParameterCount()]);
        Parameter[] parameters = executable.getParameters();

        for (int index = 0; index < parameters.length; index++) {
            handlers[index] = injector.getResources().getHandler(parameters[index]);
        }

        return handlers;
    }

    private @Nullable Object tryFetchValue(InjectorCache cache, Parameter required, int index) throws InjectorException {
        try {
            return fetchValue(cache, required, index);
        } catch (Exception e) {
            throw new InjectorException("Failed to fetch values for " + required.getDeclaringExecutable() + ", " + e.getClass() + ": " + e.getMessage(), e);
        }
    }

    private  @Nullable Object fetchValue(InjectorCache cache, Parameter required, int index) throws Exception {
        Object value = cache.getBinds()[index].getValue(required, cache.getAnnotations()[index]);

        for (InjectorResourceHandler<?, ?> handler : cache.getHandlers()[index]) {
            value = handler.process(required, ObjectUtils.cast(value));
        }

        return value;
    }

}
