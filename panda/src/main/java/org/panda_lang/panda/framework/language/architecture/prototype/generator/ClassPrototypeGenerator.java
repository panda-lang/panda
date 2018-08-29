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
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.language.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassPrototypeGenerator {

    protected static long totalLoadTime;
    protected static boolean locked;

    public ClassPrototype generate(@Nullable ModulePath modulePath, Class<?> type) {
        boolean bypass = !locked;
        long currentTime = System.nanoTime();

        if (bypass) {
            locked = true;
        }

        ClassPrototype prototype = PandaClassPrototype.builder()
                .associated(type)
                .build();

        if (modulePath != null) {
            Module module = modulePath.create(type);
            module.add(prototype);
        }

        for (Field field : type.getFields()) {
            ClassPrototypeFieldGenerator generator = new ClassPrototypeFieldGenerator(this, modulePath, prototype, field);
            PrototypeField prototypeField = generator.generate();
            prototype.getFields().addField(prototypeField);
        }

        for (Constructor<?> constructor : type.getConstructors()) {
            ClassPrototypeConstructorGenerator generator = new ClassPrototypeConstructorGenerator(this, modulePath, prototype, constructor);
            PrototypeConstructor prototypeField = generator.generate();
            prototype.getConstructors().addConstructor(prototypeField);
        }

        for (Method method : type.getMethods()) {
            ClassPrototypeMethodGenerator generator = new ClassPrototypeMethodGenerator(this, modulePath, prototype, method);
            PrototypeMethod prototypeMethod = generator.generate();
            prototype.getMethods().registerMethod(prototypeMethod);
        }

        if (bypass) {
            totalLoadTime += System.nanoTime() - currentTime;
            locked = false;
        }

        return prototype;
    }

    public ClassPrototype computeIfAbsent(ModulePath modulePath, Class<?> type) {
        Module module = modulePath.get(type);

        ClassPrototype prototype = (module == null || !module.hasClass(type))
                ? generate(modulePath, type)
                : module.get(type);

        if (prototype == null) {
            throw new PandaRuntimeException("Cannot prepare class: " + type);
        }

        module = modulePath.get(type);

        if (module == null) {
            throw new PandaRuntimeException("Cannot find module of prototype: " + prototype);
        }

        if (!module.hasClass(type)) {
            module.add(prototype);
        }

        return prototype;
    }

    public static void resetLoadTime() {
        totalLoadTime = 0;
    }

}
