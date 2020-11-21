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

package org.panda_lang.language.architecture.type.generator;

import org.panda_lang.language.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.member.constructor.TypeConstructor;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.type.member.constructor.PandaConstructor;

import java.lang.reflect.Constructor;

final class ConstructorGenerator {

    private final TypeGenerator typeGenerator;
    private final Type type;
    private final Constructor<?> constructor;

    ConstructorGenerator(TypeGenerator typeGenerator, Type type, Constructor<?> constructor) {
        this.typeGenerator = typeGenerator;
        this.type = type;
        this.constructor = constructor;
    }

    protected TypeConstructor generate(TypeLoader typeLoader) {
        PropertyParameter[] typeParameters = TypeGeneratorUtils.toParameters(typeGenerator, typeLoader, type.getModule(), constructor.getParameters());

        // TODO: Generate bytecode
        constructor.setAccessible(true);

        return PandaConstructor.builder()
                .name("constructor " + type.getSimpleName())
                .location(type.getLocation())
                .parameters(typeParameters)
                .type(type)
                .returnType(type.getSignature())
                .callback((pandaConstructor, frame, instance, arguments) -> constructor.newInstance(arguments))
                .build();
    }

}
