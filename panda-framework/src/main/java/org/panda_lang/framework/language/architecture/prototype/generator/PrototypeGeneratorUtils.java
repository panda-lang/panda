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

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.language.architecture.prototype.PandaPropertyParameter;

import java.lang.reflect.Parameter;

public final class PrototypeGeneratorUtils {

    private static final PrototypeGenerator GENERATOR = new PrototypeGenerator();

    private PrototypeGeneratorUtils() { }

    public static PandaPropertyParameter[] toParameters(Module module, Parameter[] parameters) {
        PandaPropertyParameter[] mappedParameters = new PandaPropertyParameter[parameters.length];

        for (int index = 0; index < parameters.length; index++) {
            Parameter parameter = parameters[index];
            mappedParameters[index] = new PandaPropertyParameter(index, GENERATOR.findOrGenerate(module, parameter.getType()), parameter.getName(), parameter.isVarArgs(), false);
        }

        return mappedParameters;
    }

}
