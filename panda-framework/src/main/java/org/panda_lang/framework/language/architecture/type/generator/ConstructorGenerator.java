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

package org.panda_lang.framework.language.architecture.type.generator;

import org.panda_lang.framework.design.architecture.type.PropertyParameter;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeConstructor;
import org.panda_lang.framework.design.architecture.module.TypeLoader;
import org.panda_lang.framework.language.architecture.type.PandaConstructor;

import java.lang.reflect.Constructor;

final class ConstructorGenerator {

    private final Type type;
    private final Constructor<?> constructor;

    ConstructorGenerator(Type type, Constructor<?> constructor) {
        this.type = type;
        this.constructor = constructor;
    }

    protected TypeConstructor generate(TypeLoader typeLoader) {
        PropertyParameter[] typeParameters = TypeGeneratorUtils.toParameters(typeLoader, type.getModule(), constructor.getParameters());

        // TODO: Generate bytecode
        constructor.setAccessible(true);

        return PandaConstructor.builder()
                .name("constructor " + type.getSimpleName())
                .location(type.getLocation())
                .parameters(typeParameters)
                .type(type)
                .returnType(type)
                .callback((pandaConstructor, frame, instance, arguments) -> constructor.newInstance(arguments))
                .build();
    }

}
