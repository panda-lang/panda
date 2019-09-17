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

package org.panda_lang.panda.language.architecture.prototype.standard.parameter;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.PrototypeParameter;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.architecture.dynamic.AbstractLivingFrame;
import org.panda_lang.panda.language.architecture.dynamic.AbstractLivingFrameUtils;
import org.panda_lang.panda.language.runtime.PandaRuntimeException;

public class ParameterUtils {

    public static final PrototypeParameter[] PARAMETERLESS = new PrototypeParameter[0];

    public static void assignValues(AbstractLivingFrame<?> instance, Object[] parameterValues) {
        if (instance.getAmountOfVariables() < parameterValues.length) {
            throw new PandaRuntimeException("Incompatible number of parameters");
        }

        System.arraycopy(parameterValues, 0, AbstractLivingFrameUtils.extractMemory(instance), 0, parameterValues.length);
    }

    public static ClassPrototype[] toTypes(Expression... expressions) {
        ClassPrototype[] prototypes = new ClassPrototype[expressions.length];

        for (int i = 0; i < prototypes.length; i++) {
            Expression expression = expressions[i];
            prototypes[i] = expression.getReturnType();
        }

        return prototypes;
    }

}
