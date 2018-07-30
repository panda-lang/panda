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

package org.panda_lang.panda.framework.design.architecture.prototype.mapper.generator;

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry;
import org.panda_lang.panda.framework.design.architecture.prototype.mapper.ClassPrototypeMappingGenerator;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.utilities.commons.io.PackageUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassPrototypeGenerator {

    private final ModuleRegistry registry;
    private final ClassPrototypeMappingGenerator mappingGenerator;

    public ClassPrototypeGenerator(ModuleRegistry registry, ClassPrototypeMappingGenerator mappingGenerator) {
        this.registry = registry;
        this.mappingGenerator = mappingGenerator;
    }

    public ClassPrototype generate(Class<?> type) {
        Module rootModule = registry.getOrCreate(PackageUtils.toString(type.getPackage()));
        ClassPrototype prototype = rootModule.get(type.getSimpleName());

        if (prototype != null) {
            return prototype;
        }

        prototype = new PandaClassPrototype(rootModule, type.getSimpleName(), type);
        rootModule.add(prototype);

        for (Field field : type.getFields()) {
            ClassPrototypeFieldGenerator generator = new ClassPrototypeFieldGenerator(type, prototype, field);
            PrototypeField prototypeField = generator.generate(registry);
            prototype.getFields().addField(prototypeField);
        }

        for (Constructor<?> constructor : type.getConstructors()) {
            ClassPrototypeConstructorGenerator generator = new ClassPrototypeConstructorGenerator(type, prototype, constructor);
            PrototypeConstructor prototypeField = generator.generate(registry);
            prototype.getConstructors().addConstructor(prototypeField);
        }

        for (Method method : type.getMethods()) {
            ClassPrototypeMethodGenerator generator = new ClassPrototypeMethodGenerator(type, prototype, method);
            PrototypeMethod prototypeMethod = generator.generate(registry);
            prototype.getMethods().registerMethod(prototypeMethod);
        }

        return prototype;
    }

    public ClassPrototypeMappingGenerator getMappingGenerator() {
        return mappingGenerator;
    }

}
