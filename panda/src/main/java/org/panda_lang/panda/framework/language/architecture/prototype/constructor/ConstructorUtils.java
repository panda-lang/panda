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

package org.panda_lang.panda.framework.language.architecture.prototype.constructor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

public class ConstructorUtils {

    public static final ClassPrototype[] PARAMETERLESS = new ClassPrototype[0];

    public static PrototypeConstructor matchConstructor(ClassPrototype prototype, Expression... types) {
        ClassPrototype[] expressionTypes = new ClassPrototype[types.length];

        for (int i = 0; i < expressionTypes.length; i++) {
            expressionTypes[i] = types[i].getReturnType();
        }

        return matchConstructor(prototype, expressionTypes);
    }

    public static @Nullable PrototypeConstructor matchConstructor(ClassPrototype prototype, ClassPrototype... types) {
        MATCHER:
        for (PrototypeConstructor constructor : prototype.getConstructors().getCollectionOfConstructors()) {
            ClassPrototype[] constructorTypes = constructor.getParameterTypes();

            if (constructorTypes.length != types.length) {
                continue;
            }

            for (int i = 0; i < constructorTypes.length; i++) {
                ClassPrototype constructorType = constructorTypes[i];
                ClassPrototype type = types[i];

                if (!constructorType.equals(type)) {
                    continue MATCHER;
                }
            }

            return constructor;
        }

        return null;
    }

}
