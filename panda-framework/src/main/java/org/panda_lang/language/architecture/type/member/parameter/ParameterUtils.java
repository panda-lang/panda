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

package org.panda_lang.language.architecture.type.member.parameter;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.statement.AbstractPropertyFramedScope;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.member.MemberFrameImpl;
import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.architecture.type.signature.TypedSignature;
import org.panda_lang.language.runtime.PandaRuntimeException;
import org.panda_lang.utilities.commons.function.CompletableOption;
import org.panda_lang.utilities.commons.text.Joiner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public final class ParameterUtils {

    public static final PropertyParameter[] PARAMETERLESS = new PropertyParameter[0];

    private ParameterUtils() { }

    public static void assignValues(MemberFrameImpl<? extends AbstractPropertyFramedScope> instance, Object[] parameterValues) {
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

    public static Signature[] toTypes(Expression... expressions) {
        Signature[] types = new Signature[expressions.length];

        for (int index = 0; index < types.length; index++) {
            Expression expression = expressions[index];
            types[index] = expression.getSignature();
        }

        return types;
    }

    public static Class<?>[] parametersToClasses(List<PropertyParameter> parameters) {
        return parametersToClasses(parameters.stream());
    }

    public static Class<?>[] parametersToClasses(PropertyParameter[] parameters) {
        return parametersToClasses(Arrays.stream(parameters));
    }

    private static Class<?>[] parametersToClasses(Stream<PropertyParameter> parameterStream) {
        return parameterStream
                .map(PropertyParameter::getSignature)
                .map(Signature::toTyped)
                .map(TypedSignature::fetchType)
                .map(Type::getAssociated)
                .map(CompletableOption::get)
                .toArray(Class[]::new);
    }

    public static String toString(PropertyParameter... parameters) {
        return Joiner.on(", ")
                .join(parameters, parameter -> parameter.getSignature() + " " + parameter.getName())
                .toString();
    }

}
