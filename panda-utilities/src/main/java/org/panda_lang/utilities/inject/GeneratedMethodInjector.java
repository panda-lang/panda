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

package org.panda_lang.utilities.inject;

import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.collection.Pair;
import org.panda_lang.utilities.commons.javassist.implementer.FunctionalInterfaceImplementer;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public final class GeneratedMethodInjector {

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
    public <T> T invoke(Object instance, Object... injectorArgs) throws Throwable {
        return (T) function.apply(instance, empty ? InjectorProcessor.EMPTY : processor.fetchValues(cache, method, injectorArgs));
    }

    private static BiFunction<Object, Object[], Object> generate(Method method) throws Exception {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> declaringClass = method.getDeclaringClass();
        boolean isVoid = method.getReturnType() == void.class;

        StringBuilder body = new StringBuilder("");
        body.append(declaringClass.getName()).append(" instance = (").append(declaringClass.getName()).append(") $1;").append(System.lineSeparator());
        body.append(Object.class.getName()).append("[] array = (").append(Object.class.getName()).append("[]) $2;");

        if (!isVoid) {
            body.append("return ");
        }

        body.append("instance.").append(method.getName()).append("(");

        for (int index = 0; index < parameterTypes.length; index++) {
            Class<?> parameterType = parameterTypes[index];
            String type = parameterType.getName();

            if (parameterType.isArray()) {
                Pair<Class<?>, Integer> baseClass = ArrayUtils.getBaseClassWithDimensions(parameterType);
                type = baseClass.getKey().getName() + StringUtils.repeated(baseClass.getValue(), "[]");
            }

            body.append("(").append(type).append(") array[").append(index).append("], ").append(System.lineSeparator());
        }

        if (parameterTypes.length > 0) {
            body.setLength(body.length() - (", " + System.lineSeparator()).length());
        }

        body.append(");");

        if (isVoid) {
            body.append("return null;");
        }

        String name = "PandaDI$" + ID.incrementAndGet() + "$" + method.getDeclaringClass().getSimpleName() + method.getName();
        Class<?> type = FUNCTIONAL_INTERFACE_IMPLEMENTER.generate(name, BiFunction.class, new LinkedHashMap<>(), body);

        return ObjectUtils.cast(type.newInstance());
    }

    public Method getMethod() {
        return method;
    }

}
