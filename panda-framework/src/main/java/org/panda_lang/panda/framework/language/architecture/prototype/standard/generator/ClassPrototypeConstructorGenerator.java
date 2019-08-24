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

package org.panda_lang.panda.framework.language.architecture.prototype.standard.generator;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.PrototypeParameter;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.constructor.PandaConstructor;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter.PandaParameter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

final class ClassPrototypeConstructorGenerator {

    private final ClassPrototypeGenerator generator;
    private final ClassPrototype prototype;
    private final Constructor<?> constructor;

    ClassPrototypeConstructorGenerator(ClassPrototypeGenerator generator, ClassPrototype prototype, Constructor<?> constructor) {
        this.generator = generator;
        this.prototype = prototype;
        this.constructor = constructor;
    }

    protected PrototypeConstructor generate() {
        PrototypeParameter[] prototypeParameters = new PrototypeParameter[constructor.getParameterCount()];
        Parameter[] parameters = constructor.getParameters();

        for (int index = 0; index < parameters.length; index++) {
            ClassPrototypeReference reference = generator.computeIfAbsent(prototype.getModule(), constructor.getParameterTypes()[index]);
            Parameter parameter = parameters[index];

            prototypeParameters[index] = new PandaParameter(index, reference, parameter.getName(), parameter.isVarArgs(), false);
        }

        // TODO: Generate bytecode
        constructor.setAccessible(true);

        return PandaConstructor.builder()
                .name("constructor " + prototype.getName())
                .parameters(prototypeParameters)
                .prototype(prototype.getReference())
                .returnType(prototype.getReference())
                .callback((frame, instance, arguments) -> constructor.newInstance(arguments))
                .build();
    }

}
