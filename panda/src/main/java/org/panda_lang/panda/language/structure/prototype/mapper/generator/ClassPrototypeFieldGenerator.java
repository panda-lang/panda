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

package org.panda_lang.panda.language.structure.prototype.mapper.generator;

import org.panda_lang.panda.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.field.FieldVisibility;
import org.panda_lang.panda.design.architecture.prototype.field.implementation.PandaPrototypeField;
import org.panda_lang.panda.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.design.architecture.prototype.field.StaticValue;
import org.panda_lang.panda.design.architecture.value.PandaValue;
import org.panda_lang.panda.design.runtime.PandaRuntimeException;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.overall.module.ModuleRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ClassPrototypeFieldGenerator {

    private final Class<?> type;
    private final ClassPrototype prototype;
    private final Field field;

    public ClassPrototypeFieldGenerator(Class<?> type, ClassPrototype prototype, Field field) {
        this.type = type;
        this.prototype = prototype;
        this.field = field;
    }

    public PrototypeField generate() {
        ClassPrototype returnType = ModuleRegistry.forClass(field.getType());
        PrototypeField prototypeField = new PandaPrototypeField(returnType, prototype.getFields().size(), field.getName(), FieldVisibility.PUBLIC, Modifier.isStatic(field.getModifiers()), true);

        // TODO: Generate bytecode
        Expression fieldExpression = new Expression(returnType, (expression, branch) -> {
            Object instance = branch != null ? branch.getInstance().getValue() : null;

            try {
                Object value = field.get(instance);
                return new PandaValue(returnType, value);
            } catch (IllegalAccessException e) {
                throw new PandaRuntimeException(e);
            }
        });

        prototypeField.setDefaultValue(fieldExpression);
        prototypeField.setStaticValue(StaticValue.of(fieldExpression, null));
        return prototypeField;
    }

}
