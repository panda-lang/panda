/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.architecture.type.generator;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.expression.ExpressionUtils;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.type.member.field.TypeField;
import panda.interpreter.architecture.module.TypeLoader;
import panda.interpreter.runtime.ProcessStack;
import panda.interpreter.architecture.expression.AbstractDynamicExpression;
import panda.interpreter.architecture.expression.PandaExpression;
import panda.interpreter.architecture.type.member.field.PandaField;
import panda.interpreter.runtime.PandaRuntimeException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

final class FieldGenerator {

    private final TypeGenerator generator;
    private final Type type;
    private final Field field;

    FieldGenerator(TypeGenerator generator, Type type, Field field) {
        this.generator = generator;
        this.type = type;
        this.field = field;
    }

    protected TypeField generate(TypeLoader typeLoader) {
        TypeField typeField = PandaField.builder()
                .name(field.getName())
                .type(type)
                .location(type.getLocation())
                .fieldIndex(type.getFields().size())
                .returnType(generator.findOrGenerate(typeLoader, type.getModule(), field.getType()).getSignature())
                .isStatic(Modifier.isStatic(field.getModifiers()))
                .mutable(!Modifier.isFinal(field.getModifiers()))
                .isNative(true)
                .nillable(true)
                .build();
        // TODO: Generate bytecode
        field.setAccessible(true);

        Expression fieldExpression = new PandaExpression(new AbstractDynamicExpression(typeField.getReturnType()) {
            @Override
            @SuppressWarnings("unchecked")
            public Object evaluate(ProcessStack flow, Object instance) {
                try {
                    return field.get(instance);
                } catch (IllegalAccessException illegalAccessException) {
                    throw new PandaRuntimeException("Cannot get value of " + field, illegalAccessException);
                }
            }
        });

        typeField.setDefaultValue(fieldExpression);
        typeField.setStaticValue(typeField.isStatic() ? () -> ExpressionUtils.evaluateConstExpression(fieldExpression) : null);
        return typeField;
    }

}
