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
import org.panda_lang.framework.design.architecture.prototype.PrototypeConstructor;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.language.architecture.prototype.PandaConstructor;
import org.panda_lang.framework.language.architecture.prototype.PandaPropertyParameter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

final class ConstructorGenerator {

    private final PrototypeGenerator generator;
    private final Prototype prototype;
    private final Constructor<?> constructor;

    ConstructorGenerator(PrototypeGenerator generator, Prototype prototype, Constructor<?> constructor) {
        this.generator = generator;
        this.prototype = prototype;
        this.constructor = constructor;
    }

    protected PrototypeConstructor generate() {
        PropertyParameter[] prototypeParameters = new PropertyParameter[constructor.getParameterCount()];
        Parameter[] parameters = constructor.getParameters();

        for (int index = 0; index < parameters.length; index++) {
            Reference reference = generator.findOrGenerate(prototype.getModule(), constructor.getParameterTypes()[index]);
            Parameter parameter = parameters[index];

            prototypeParameters[index] = new PandaPropertyParameter(index, reference, parameter.getName(), parameter.isVarArgs(), false);
        }

        // TODO: Generate bytecode
        constructor.setAccessible(true);

        return PandaConstructor.builder()
                .name("constructor " + prototype.getName())
                .location(prototype.getLocation())
                .parameters(prototypeParameters)
                .prototype(prototype.toReference())
                .returnType(prototype.toReference())
                .callback((frame, instance, arguments) -> constructor.newInstance(arguments))
                .build();
    }

}
