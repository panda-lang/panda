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

package org.panda_lang.framework.language.architecture.prototype.generator;

import org.panda_lang.framework.design.architecture.prototype.PropertyParameter;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMethod;
import org.panda_lang.framework.language.architecture.prototype.PandaMethod;
import org.panda_lang.framework.language.architecture.prototype.PrototypeExecutableCallback;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.InvalidParameterException;

final class MethodGenerator {

    private final PrototypeGenerator generator;
    private final Prototype prototype;
    private final Method method;

    MethodGenerator(PrototypeGenerator generator, Prototype prototype, Method method) {
        if (method == null) {
            throw new InvalidParameterException("Method cannot be null");
        }

        this.generator = generator;
        this.prototype = prototype;
        this.method = method;
    }

    protected PrototypeMethod generate() {
        PropertyParameter[] mappedParameters = PrototypeGeneratorUtils.toParameters(prototype.getModule(), method.getParameters());
        // TODO: Generate bytecode
        method.setAccessible(true);

        PrototypeExecutableCallback<Object> methodBody = (stack, instance, arguments) -> {
            int amountOfArgs = arguments.length;
            int parameterCount = method.getParameterCount();
            Object varargs = null;

            if (amountOfArgs != parameterCount) {
                if (parameterCount < 1) {
                    throw new PandaRuntimeException("Too many arguments");
                }

                Class<?> last = method.getParameterTypes()[parameterCount - 1];
                String lastName = last.getName();
                Class<?> rootLast = Class.forName(lastName.substring(2, lastName.length() - 1));

                if (amountOfArgs + 1 != parameterCount || !last.isArray()) {
                    throw new PandaRuntimeException("Cannot invoke mapped mapped method (args.length != parameters.length)");
                }

                varargs = Array.newInstance(rootLast, 0);
                amountOfArgs++;
            }

            if (varargs != null) {
                if (arguments.length < amountOfArgs) {
                    throw new PandaRuntimeException("Varargs not allowed");
                }

                arguments[amountOfArgs - 1] = varargs;
            }

            try {
                return method.invoke(instance, arguments);
            } catch (InvocationTargetException e) {
                Throwable throwable = e.getTargetException();

                if (throwable instanceof Exception) {
                    throw (Exception) throwable;
                }

                throw new PandaRuntimeException("Internal error", throwable);
            }
        };

        return PandaMethod.builder()
                .name(method.getName())
                .prototype(prototype)
                .isNative(true)
                .isStatic(Modifier.isStatic(method.getModifiers()))
                .returnType(generator.findOrGenerate(prototype.getModule(), method.getReturnType()))
                .location(prototype.getLocation())
                .methodBody(methodBody)
                .parameters(mappedParameters)
                .build();
    }

}
