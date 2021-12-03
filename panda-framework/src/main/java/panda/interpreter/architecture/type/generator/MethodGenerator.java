/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.architecture.type.generator;

import panda.interpreter.FrameworkController;
import panda.interpreter.architecture.module.TypeLoader;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.type.member.MemberInvoker;
import panda.interpreter.architecture.type.member.method.PandaMethod;
import panda.interpreter.architecture.type.member.method.TypeMethod;
import panda.interpreter.architecture.type.member.parameter.PropertyParameter;
import panda.interpreter.architecture.type.signature.GenericSignature;
import panda.interpreter.architecture.type.signature.Relation;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.runtime.PandaRuntimeException;
import panda.interpreter.token.PandaSnippet;
import panda.utilities.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.security.InvalidParameterException;
import java.util.List;

final class MethodGenerator {

    private final FrameworkController controller;
    private final TypeGenerator generator;
    private final Type type;
    private final Method method;

    MethodGenerator(FrameworkController controller, TypeGenerator generator, Type type, Method method) {
        if (method == null) {
            throw new InvalidParameterException("Method cannot be null");
        }

        this.controller = controller;
        this.generator = generator;
        this.type = type;
        this.method = method;
    }

    TypeMethod generate(TypeLoader typeLoader) {
        // TODO: Generate bytecode
        method.setAccessible(true);

        MemberInvoker<TypeMethod, Object, Object> methodBody = (typeMethod, stack, instance, arguments) -> {
            int amountOfArgs = arguments.length;
            int parameterCount = method.getParameterCount();
            Object varargs = null;

            if (amountOfArgs != parameterCount) {
                if (parameterCount < 1) {
                    throw new PandaRuntimeException("Too many arguments");
                }

                Class<?> last = method.getParameterTypes()[parameterCount - 1];
                String lastName = last.getName();
                Class<?> rootLast = Class.forName(lastName.substring(2, lastName.length() - 1), true, controller.getClassLoader());

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

            if (parameterCount == 1) {
                if (method.isVarArgs()) {
                    Object array = Array.newInstance(method.getParameters()[0].getType().getComponentType(), 1);
                    Array.set(array, 0, arguments[0]);
                    arguments[0] = array;
                }
            }

            try {
                return method.invoke(instance, arguments);
            } catch (InvocationTargetException invocationTargetException) {
                Throwable throwable = invocationTargetException.getTargetException();

                if (throwable instanceof Exception) {
                    throw (Exception) throwable;
                }

                throw new PandaRuntimeException("Internal error", throwable);
            } catch (IllegalArgumentException illegalArgumentException) {
                illegalArgumentException.printStackTrace();
                throw illegalArgumentException;
            }
        };

        List<? extends PropertyParameter> mappedParameters = TypeGeneratorUtils.toParameters(generator, typeLoader, type.getModule(), method.getParameters());

        Signature returnType = null;
        java.lang.reflect.Type genericReturnType = method.getGenericReturnType();

        if (genericReturnType instanceof ParameterizedType) {
            ParameterizedType parameterizedReturnType = (ParameterizedType) genericReturnType;
            Class<?> rawType = ClassUtils.forName(parameterizedReturnType.getRawType().getTypeName()).get();

            java.lang.reflect.Type[] actualTypeArguments = parameterizedReturnType.getActualTypeArguments();
            Signature[] signatures = new Signature[actualTypeArguments.length];

            for (int index = 0; index < actualTypeArguments.length; index++) {
                signatures[index] = new GenericSignature(typeLoader, null, actualTypeArguments[index].getTypeName(), null, new Signature[0], Relation.DIRECT, PandaSnippet.empty());
            }

            returnType = new GenericSignature(typeLoader, null, rawType.getSimpleName(), null, signatures, Relation.DIRECT, PandaSnippet.empty());
        }

        if (returnType == null) {
            returnType = generator.findOrGenerate(typeLoader, type.getModule(), method.getReturnType()).getSignature();
        }

        return PandaMethod.builder()
                .name(method.getName())
                .type(type)
                .isNative(true)
                .isStatic(Modifier.isStatic(method.getModifiers()))
                .returnType(returnType)
                .location(type.getLocation())
                .customBody(methodBody)
                .parameters(mappedParameters)
                .build();
    }

}
