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

package org.panda_lang.framework.language.architecture.parameter;

import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.parameter.Parameter;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.language.architecture.dynamic.AbstractFrame;
import org.panda_lang.framework.language.architecture.dynamic.AbstractLivingFrameUtils;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

public class ParameterUtils {

    public static final Parameter[] PARAMETERLESS = new Parameter[0];

    public static void assignValues(AbstractFrame<?> instance, Object[] parameterValues) {
        if (instance.getAmountOfVariables() < parameterValues.length) {
            throw new PandaRuntimeException("Incompatible number of parameters");
        }

        System.arraycopy(parameterValues, 0, AbstractLivingFrameUtils.extractMemory(instance), 0, parameterValues.length);
    }

    public static Prototype[] toTypes(Expression... expressions) {
        Prototype[] prototypes = new Prototype[expressions.length];

        for (int index = 0; index < prototypes.length; index++) {
            Expression expression = expressions[index];
            prototypes[index] = expression.getReturnType();
        }

        return prototypes;
    }

}
