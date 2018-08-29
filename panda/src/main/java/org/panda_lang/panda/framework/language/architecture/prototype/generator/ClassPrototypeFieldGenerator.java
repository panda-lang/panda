/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.architecture.prototype.generator;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.FieldVisibility;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.field.PandaPrototypeField;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ClassPrototypeFieldGenerator {

    private final ClassPrototypeGenerator generator;
    private final @Nullable ModulePath modulePath;
    private final ClassPrototype prototype;
    private final Field field;

    public ClassPrototypeFieldGenerator(ClassPrototypeGenerator generator, @Nullable ModulePath modulePath, ClassPrototype prototype, Field field) {
        this.generator = generator;
        this.modulePath = modulePath;
        this.prototype = prototype;
        this.field = field;
    }

    public PrototypeField generate() {
        ClassPrototype returnType = generator.computeIfAbsent(modulePath, field.getType());

        PrototypeField prototypeField = PandaPrototypeField.builder()
                .fieldIndex(prototype.getFields().getAmountOfFields())
                .type(returnType)
                .name(field.getName())
                .visibility(FieldVisibility.PUBLIC)
                .isStatic(Modifier.isStatic(field.getModifiers()))
                .mutable(true)
                .nullable(true)
                .build();

        // TODO: Generate bytecode
        field.setAccessible(true);

        Expression fieldExpression = new PandaExpression(returnType, (expression, branch) -> {
            long start = System.nanoTime();
            Object instance = branch != null ? branch.getInstance().getValue() : null;

            try {
                Object value = field.get(instance);
                return new PandaValue(returnType, value);
            } catch (IllegalAccessException e) {
                throw new PandaRuntimeException(e);
            } finally {
                ClassPrototypeGeneratorManager.reflectionsTime += System.nanoTime() - start;
            }
        });

        prototypeField.setDefaultValue(fieldExpression);
        prototypeField.setStaticValue(PandaStaticValue.of(fieldExpression, null));
        return prototypeField;
    }

}
