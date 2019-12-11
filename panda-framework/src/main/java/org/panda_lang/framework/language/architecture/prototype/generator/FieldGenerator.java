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

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.expression.ExpressionUtils;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.expression.AbstractDynamicExpression;
import org.panda_lang.framework.language.architecture.expression.PandaExpression;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototypeField;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

final class FieldGenerator {

    private final PrototypeGenerator generator;
    private final Prototype prototype;
    private final Field field;

    FieldGenerator(PrototypeGenerator generator, Prototype prototype, Field field) {
        this.generator = generator;
        this.prototype = prototype;
        this.field = field;
    }

    protected PrototypeField generate() {
        PrototypeField prototypeField = PandaPrototypeField.builder()
                .name(field.getName())
                .prototype(prototype)
                .location(prototype.getLocation())
                .fieldIndex(prototype.getFields().getDeclaredProperties().size())
                .returnType(generator.findOrGenerate(prototype.getModule(), field.getType()))
                .isStatic(Modifier.isStatic(field.getModifiers()))
                .isNative(true)
                .mutable(true)
                .nillable(true)
                .build();

        // TODO: Generate bytecode
        field.setAccessible(true);

        Expression fieldExpression = new PandaExpression(new AbstractDynamicExpression(prototypeField.getType()) {
            @Override
            @SuppressWarnings("unchecked")
            public Object evaluate(ProcessStack flow, Object instance) {
                try {
                    return field.get(instance);
                } catch (IllegalAccessException e) {
                    throw new PandaRuntimeException("Cannot get value of " + field, e);
                }
            }
        });

        prototypeField.setDefaultValue(fieldExpression);
        prototypeField.setStaticValue(prototypeField.isStatic() ? () -> ExpressionUtils.evaluateConstExpression(fieldExpression) : null);

        return prototypeField;
    }

}
