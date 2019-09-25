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

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeConstructor;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMethod;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototype;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;
import org.panda_lang.utilities.commons.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

final class ClassPrototypeGenerator {

    protected static boolean locked;

    protected void generate(Module module, Class<?> type, String name) {
        boolean bypass = !locked;

        if (bypass) {
            locked = true;
        }

        module.add(name, type, () -> {
            Prototype prototype = PandaPrototype.builder()
                    .module(module)
                    .name(name)
                    .associated(type)
                    .build();

            prototype.getReference().addInitializer(() -> {
                if (!Modifier.isPublic(type.getModifiers())) {
                    return;
                }

                for (Field field : type.getFields()) {
                    if (!Modifier.isPublic(field.getModifiers())) {
                        continue;
                    }

                    ClassPrototypeFieldGenerator generator = new ClassPrototypeFieldGenerator(this, prototype, field);
                    PrototypeField prototypeField = generator.generate();
                    prototype.getFields().declare(prototypeField);
                }

                for (Constructor<?> constructor : ReflectionUtils.getByModifier(type.getConstructors(), Modifier.PUBLIC)) {
                    ClassPrototypeConstructorGenerator generator = new ClassPrototypeConstructorGenerator(this, prototype, constructor);
                    PrototypeConstructor prototypeField = generator.generate();
                    prototype.getConstructors().declare(prototypeField);
                }

                for (Method method : ReflectionUtils.getByModifier(type.getMethods(), Modifier.PUBLIC)) {
                    ClassPrototypeMethodGenerator generator = new ClassPrototypeMethodGenerator(this, prototype, method);
                    PrototypeMethod prototypeMethod = generator.generate();
                    prototype.getMethods().declare(prototypeMethod);
                }

                if (bypass) {
                    locked = false;
                }
            });

            return prototype.getReference();
        });
    }

    protected PrototypeReference computeIfAbsent(Module module, Class<?> type) {
        if (!module.hasPrototype(type)) {
            generate(module, type, type.getSimpleName());
        }

        return module.forName(type.getSimpleName()).orElseThrow((Supplier<? extends PandaRuntimeException>) () -> {
            throw new PandaRuntimeException("Cannot prepare class: " + type);
        });
    }

}
