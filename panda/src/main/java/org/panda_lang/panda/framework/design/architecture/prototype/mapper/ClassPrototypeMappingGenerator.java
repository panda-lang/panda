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

package org.panda_lang.panda.framework.design.architecture.prototype.mapper;

import org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry;
import org.panda_lang.panda.framework.design.architecture.prototype.mapper.generator.ClassPrototypeGenerator;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;

import java.util.ArrayList;
import java.util.Collection;

public class ClassPrototypeMappingGenerator {

    private final ClassPrototypeMappingManager mappingManager;

    public ClassPrototypeMappingGenerator(ClassPrototypeMappingManager mappingManager) {
        this.mappingManager = mappingManager;
    }

    public Collection<ClassPrototype> generate(ModuleRegistry registry, Collection<Class<?>> classes) {
        ClassPrototypeGenerator generator = new ClassPrototypeGenerator(registry, this);
        Collection<ClassPrototype> prototypes = new ArrayList<>(classes.size());

        for (Class<?> clazz : classes) {
            ClassPrototype prototype = generator.generate(clazz);
            prototypes.add(prototype);
        }

        return prototypes;
    }

    protected ClassPrototypeMappingManager getMappingManager() {
        return mappingManager;
    }

}
