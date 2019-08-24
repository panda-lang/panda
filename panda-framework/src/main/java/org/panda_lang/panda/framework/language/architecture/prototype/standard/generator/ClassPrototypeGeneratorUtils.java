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

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter.PandaParameter;

import java.lang.reflect.Parameter;

public class ClassPrototypeGeneratorUtils {

    private static final ClassPrototypeGenerator GENERATOR = new ClassPrototypeGenerator();

    public static PandaParameter[] toParameters(Module module, Parameter[] parameters) {
        PandaParameter[] mappedParameters = new PandaParameter[parameters.length];

        for (int index = 0; index < parameters.length; index++) {
            Parameter parameter = parameters[index];
            mappedParameters[index] = new PandaParameter(index, GENERATOR.computeIfAbsent(module, parameter.getType()), parameter.getName(), parameter.isVarArgs(), false);
        }

        return mappedParameters;
    }

    public static ClassPrototypeReference[] toTypes(Module module, Class<?>... types) {
        ClassPrototypeGenerator generator = new ClassPrototypeGenerator();
        ClassPrototypeReference[] references = new ClassPrototypeReference[types.length];

        for (int i = 0; i < types.length; i++) {
            references[i] = generator.computeIfAbsent(module, types[i]);
        }

        return references;
    }

}
