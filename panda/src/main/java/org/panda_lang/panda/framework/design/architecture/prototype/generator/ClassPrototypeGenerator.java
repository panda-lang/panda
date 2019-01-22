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

package org.panda_lang.panda.framework.design.architecture.prototype.generator;

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassPrototypeGenerator {

    protected static long totalLoadTime;
    protected static boolean locked;

    public ClassPrototypeReference generate(Module module, Class<?> type) {
        boolean bypass = !locked;
        long currentTime = System.nanoTime();

        if (bypass) {
            locked = true;
        }

        ClassPrototype prototype = PandaClassPrototype.builder()
                .module(module)
                .associated(type)
                .build();

        prototype.getReference().addInitializer(() -> {
            for (Field field : type.getFields()) {
                ClassPrototypeFieldGenerator generator = new ClassPrototypeFieldGenerator(this, prototype, field);
                PrototypeField prototypeField = generator.generate();
                prototype.getFields().addField(prototypeField);
            }

            for (Constructor<?> constructor : type.getConstructors()) {
                ClassPrototypeConstructorGenerator generator = new ClassPrototypeConstructorGenerator(this, prototype, constructor);
                PrototypeConstructor prototypeField = generator.generate();
                prototype.getConstructors().addConstructor(prototypeField);
            }

            for (Method method : type.getMethods()) {
                ClassPrototypeMethodGenerator generator = new ClassPrototypeMethodGenerator(this, prototype, method);
                PrototypeMethod prototypeMethod = generator.generate();
                prototype.getMethods().registerMethod(prototypeMethod);
            }

            if (bypass) {
                totalLoadTime += System.nanoTime() - currentTime;
                locked = false;
            }
        });

        return module.add(prototype.getReference());
    }

    public ClassPrototypeReference computeIfAbsent(Module module, Class<?> type) {
        ClassPrototypeReference reference = (module == null || !module.hasClass(type))
                ? generate(module, type)
                : module.getAssociatedWith(type).get();

        if (reference == null) {
            throw new PandaRuntimeException("Cannot prepare class: " + type);
        }

        if (module == null) {
            throw new PandaRuntimeException("Cannot find module of prototype: " + reference);
        }

        if (!module.hasClass(type)) {
            module.add(reference);
        }

        return reference;
    }

    public static void resetLoadTime() {
        totalLoadTime = 0;
    }

}
