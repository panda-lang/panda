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

package org.panda_lang.framework.language.architecture.type.utils;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.type.PropertyParameter;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.language.architecture.statement.AbstractPropertyFramedScope;
import org.panda_lang.framework.language.architecture.statement.PandaPropertyFrame;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;
import org.panda_lang.utilities.commons.text.ContentJoiner;

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

    public static Type[] toTypes(Expression... expressions) {
        Type[] types = new Type[expressions.length];

        for (int index = 0; index < types.length; index++) {
            Expression expression = expressions[index];
            types[index] = expression.getType();
        }

        return types;
    }

    public static String toString(PropertyParameter... parameters) {
        return ContentJoiner.on(", ")
                .join(parameters, parameter -> parameter.getType().getName() + " " + parameter.getName())
                .toString();
    }

}
