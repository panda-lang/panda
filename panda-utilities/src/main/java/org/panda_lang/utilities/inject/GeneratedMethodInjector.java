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

import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.javassist.implementer.FunctionalInterfaceImplementer;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

final class GeneratedMethodInjector {

    private static final AtomicInteger ID = new AtomicInteger();
    private static final FunctionalInterfaceImplementer FUNCTIONAL_INTERFACE_IMPLEMENTER =  new FunctionalInterfaceImplementer();

    private final InjectorProcessor processor;
    private final Method method;
    private final BiFunction<Object, Object[], Object> function;
    private final InjectorCache cache;
    private final boolean empty;

    GeneratedMethodInjector(InjectorProcessor processor, Method method) throws Exception {
        this.processor = processor;
        this.method = method;
        this.function = generate(method);
        this.cache = InjectorCache.of(processor, method);
        this.empty = method.getParameterCount() == 0;
    }

    @SuppressWarnings("unchecked")
    public <T> T invoke(Object instance) {
        return (T) function.apply(instance, empty ? InjectorProcessor.EMPTY : processor.fetchValues(cache, method));
    }

    private static BiFunction<Object, Object[], Object> generate(Method method) throws Exception {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> declaringClass = method.getDeclaringClass();

        StringBuilder body = new StringBuilder("").append(declaringClass.getName()).append(" instance = (").append(declaringClass.getName()).append(") $1;");
        body.append("return instance.").append(method.getName()).append("(");

        for (int index = 0; index < parameterTypes.length; index++) {
            body.append("(").append(parameterTypes[index].getName()).append(") $2[").append(index).append("], ");
        }

        if (parameterTypes.length > 0) {
            body.setLength(body.length() - 2);
        }

        body.append(");");

        String name = "PandaDI" + method.getDeclaringClass().getSimpleName() + method.getName();
        Class<?> type = FUNCTIONAL_INTERFACE_IMPLEMENTER.generate(name, BiFunction.class, new LinkedHashMap<>(), body);

        return ObjectUtils.cast(type.newInstance());
    }

}
