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

package org.panda_lang.framework.language.architecture.prototype.utils;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.prototype.PropertyParameter;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.language.architecture.statement.AbstractPropertyFramedScope;
import org.panda_lang.framework.language.architecture.statement.PandaPropertyFrame;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

import java.util.List;

public final class ParameterUtils {

    public static final PropertyParameter[] PARAMETERLESS = new PropertyParameter[0];

    private ParameterUtils() { }

    public static void assignValues(PandaPropertyFrame<? extends AbstractPropertyFramedScope> instance, Object[] parameterValues) {
        if (instance.getMemorySize() < parameterValues.length) {
            throw new PandaRuntimeException("Incompatible number of parameters");
        }

        List<? extends PropertyParameter> parameters = instance.getFramedScope().getParameters();

        for (int index = 0; index < parameterValues.length; index++) {
            Object value = parameterValues[index];

            if (value == null) {
                PropertyParameter parameter = parameters.get(index);

                if (!parameter.isNillable()) {
                    throw new PandaRuntimeException("Cannot assign null to  the '" + parameter.getName() + "' parameter without nil modifier");
                }
            }

            instance.set(index, value);
        }
    }

    public static Prototype[] toTypes(Expression... expressions) {
        Prototype[] prototypes = new Prototype[expressions.length];

        for (int index = 0; index < prototypes.length; index++) {
            Expression expression = expressions[index];
            prototypes[index] = expression.getType();
        }

        return prototypes;
    }

}
