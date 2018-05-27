/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.language.interpreter.parsers.prototype.mapper.generator;

import org.panda_lang.panda.design.architecture.module.*;
import org.panda_lang.panda.design.architecture.prototype.PandaClassPrototypeUtils;
import org.panda_lang.panda.design.architecture.prototype.method.PandaMethod;
import org.panda_lang.panda.design.runtime.PandaRuntimeException;
import org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodCallback;
import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodVisibility;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.InvalidParameterException;

public class ClassPrototypeMethodGenerator {

    private final Class<?> type;
    private final ClassPrototype prototype;
    private final Method method;

    public ClassPrototypeMethodGenerator(Class<?> type, ClassPrototype prototype, Method method) {
        if (method == null) {
            throw new InvalidParameterException("Method cannot be null");
        }

        this.type = type;
        this.prototype = prototype;
        this.method = method;
    }

    public PrototypeMethod generate(ModuleRegistry registry) {
        ClassPrototype returnType = PandaModuleRegistryAssistant.forClass(registry, method.getReturnType());
        ClassPrototype[] parametersTypes = PandaClassPrototypeUtils.toTypes(registry, method.getParameterTypes());
        boolean isVoid = returnType.getClassName().equals("void");

        // TODO: Generate bytecode
        MethodCallback<Object> methodBody = (branch, instance, parameters) -> {
            try {
                int amountOfArgs = parameters.length;
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
                    ++amountOfArgs;
                }

                Object[] args = new Object[amountOfArgs];

                for (int i = 0; i < parameters.length; i++) {
                    Value parameter = parameters[i];

                    if (parameter == null) {
                        continue;
                    }

                    args[i] = parameter.getValue();
                }

                if (varargs != null) {
                    args[amountOfArgs - 1] = varargs;
                }

                Object returnValue = method.invoke(instance, args);

                if (isVoid) {
                    return;
                }

                Value value = new PandaValue(returnType, returnValue);
                branch.setReturnValue(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        return PandaMethod.builder()
                .prototype(prototype)
                .visibility(MethodVisibility.PUBLIC)
                .isStatic(Modifier.isStatic(method.getModifiers()))
                .returnType(returnType)
                .methodName(method.getName())
                .methodBody(methodBody)
                .parameterTypes(parametersTypes)
                .build();
    }

}
