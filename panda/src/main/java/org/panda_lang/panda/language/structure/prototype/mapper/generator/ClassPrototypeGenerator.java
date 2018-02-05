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
import org.panda_lang.panda.language.structure.prototype.structure.constructor.PrototypeConstructor;
import org.panda_lang.panda.language.structure.prototype.structure.field.PrototypeField;
import org.panda_lang.panda.language.structure.prototype.structure.method.PrototypeMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassPrototypeGenerator {

    private final ClassPrototypeMappingGenerator mappingGenerator;

    public ClassPrototypeGenerator(ClassPrototypeMappingGenerator mappingGenerator) {
        this.mappingGenerator = mappingGenerator;
    }

    public ClassPrototype generate(Class<?> type) {
        Module module = ModuleRegistry.getDefault().getOrCreate(type.getPackage().getName());
        ClassPrototype prototype = module.get(type.getSimpleName());

        if (prototype != null) {
            return prototype;
        }

        prototype = new ClassPrototype(module, type.getSimpleName());
        prototype.getAssociated().add(type);

        for (Field field : type.getFields()) {
            ClassPrototypeFieldGenerator generator = new ClassPrototypeFieldGenerator(type, prototype, field);
            PrototypeField prototypeField = generator.generate();
            prototype.getFields().add(prototypeField);
        }

        for (Constructor<?> constructor : type.getConstructors()) {
            ClassPrototypeConstructorGenerator generator = new ClassPrototypeConstructorGenerator(type, prototype, constructor);
            PrototypeConstructor prototypeField = generator.generate();
            prototype.getConstructors().add(prototypeField);
        }

        for (Method method : type.getMethods()) {
            switch (method.getName()) {
                case "finalize":
                case "notify":
                case "notifyAll":
                case "wait":
                    continue;
            }

            ClassPrototypeMethodGenerator generator = new ClassPrototypeMethodGenerator(type, prototype, method);
            PrototypeMethod prototypeMethod = generator.generate();
            prototype.getMethods().registerMethod(prototypeMethod);
        }

        return prototype;
    }

}
