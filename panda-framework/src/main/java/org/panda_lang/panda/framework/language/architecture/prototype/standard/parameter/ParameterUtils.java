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

package org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeMetadata;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.ParameterizedExecutable;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.PrototypeParameter;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.language.architecture.dynamic.AbstractScopeFrame;
import org.panda_lang.panda.framework.language.architecture.dynamic.AbstractScopeFrameUtils;

import java.util.Collection;
import java.util.List;

public class ParameterUtils {

    public static final PrototypeParameter[] PARAMETERLESS = new PrototypeParameter[0];

    public static void addAll(List<Variable> variables, List<? extends PrototypeParameter> parameters) {
        for (PrototypeParameter parameter : parameters) {
            Variable variable = parameter.toVariable();
            variables.add(variable);
        }
    }

    public static void assignValues(AbstractScopeFrame<?> instance, Value[] parameterValues) {
        if (instance.getAmountOfVariables() < parameterValues.length) {
            throw new RuntimeException("Incompatible number of parameters");
        }

        System.arraycopy(parameterValues, 0, AbstractScopeFrameUtils.extractMemory(instance), 0, parameterValues.length);
    }

    public static <T extends ParameterizedExecutable> @Nullable T match(Collection<T> collection, ClassPrototypeMetadata... requiredTypes) {
        for (T executable : collection) {
            if (matchParameters(executable, requiredTypes)) {
                return executable;
            }
        }

        return null;
    }

    public static boolean matchParameters(ParameterizedExecutable executable, ClassPrototypeMetadata... requiredTypes) {
        PrototypeParameter[] parameters = executable.getParameters();

        for (int required = 0, index = 0; required < requiredTypes.length; required++) {
            if (index >= parameters.length) {
                return false;
            }

            PrototypeParameter parameter = parameters[index++];
            ClassPrototypeMetadata requiredType = requiredTypes[required];

            if (parameter.isVarargs()) {
                while (required < requiredTypes.length) {
                    if (!parameter.getType().isAssignableFrom(requiredType)) {
                        break;
                    }

                    required++;
                }
            }
            else if (!parameter.getType().isAssignableFrom(requiredType)) {
                return false;
            }
        }

        return true;
    }

}
