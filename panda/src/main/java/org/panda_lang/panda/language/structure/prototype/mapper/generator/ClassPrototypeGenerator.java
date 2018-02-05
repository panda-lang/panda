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

import org.panda_lang.panda.language.structure.overall.module.Module;
import org.panda_lang.panda.language.structure.overall.module.ModuleRegistry;
import org.panda_lang.panda.language.structure.prototype.mapper.ClassPrototypeMappingGenerator;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassPrototypeGenerator {

    private final ClassPrototypeMappingGenerator mappingGenerator;

    public ClassPrototypeGenerator(ClassPrototypeMappingGenerator mappingGenerator) {
        this.mappingGenerator = mappingGenerator;
    }

    public ClassPrototype generate(Class<?> clazz) {
        Module module = ModuleRegistry.getDefault().getOrCreate(clazz.getPackage().getName());
        ClassPrototype classPrototype = new ClassPrototype(module, clazz.getSimpleName());

        for (Field field : clazz.getFields()) {
            // TODO: Impl fields
        }

        for (Constructor<?> constructor : clazz.getConstructors()) {
            ClassPrototypeConstructorGenerator generator = new ClassPrototypeConstructorGenerator(clazz, classPrototype, constructor);

        }

        for (Method method : clazz.getMethods()) {
            // TODO: Impl methods
        }

        return classPrototype;
    }

}
