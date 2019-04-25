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

package org.panda_lang.panda.framework.design.architecture.prototype.parameter;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.language.architecture.dynamic.AbstractScopeFrame;
import org.panda_lang.panda.framework.language.architecture.dynamic.AbstractScopeFrameUtils;

import java.util.List;

public class ParameterUtils {


    public static void addAll(List<Variable> variables, List<? extends Parameter> parameters, int nestingLevel) {
        for (Parameter parameter : parameters) {
            Variable variable = parameter.toVariable(nestingLevel);
            variables.add(variable);
        }
    }

    public static void assignValues(AbstractScopeFrame<?> instance, Value[] parameterValues) {
        if (instance.getAmountOfVariables() < parameterValues.length) {
            throw new RuntimeException("Incompatible number of parameters");
        }

        System.arraycopy(parameterValues, 0, AbstractScopeFrameUtils.extractMemory(instance), 0, parameterValues.length);
    }

    public static ClassPrototypeReference[] toTypes(List<? extends Parameter> parameters) {
        ClassPrototypeReference[] references = new ClassPrototypeReference[parameters.size()];

        for (int i = 0; i < references.length; i++) {
            Parameter parameter = parameters.get(i);
            references[i] = parameter.getParameterType();
        }

        return references;
    }

}
