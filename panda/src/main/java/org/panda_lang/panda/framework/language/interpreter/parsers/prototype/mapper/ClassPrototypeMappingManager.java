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

package org.panda_lang.panda.framework.language.interpreter.parsers.prototype.mapper;

import org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.language.interpreter.parsers.prototype.mapper.loaders.ClassPrototypeMappingAnnotationLoader;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

import java.util.ArrayList;
import java.util.Collection;

public class ClassPrototypeMappingManager {

    private final Collection<Class<?>> loadedClasses;
    private long totalLoadTime;

    public ClassPrototypeMappingManager() {
        this.loadedClasses = new ArrayList<>();
    }

    public void loadAnnotatedMappings() {
        ClassPrototypeMappingAnnotationLoader loader = new ClassPrototypeMappingAnnotationLoader(this);
        Collection<Class<?>> classes = loader.load();

        for (Class<?> clazz : classes) {
            this.loadClass(clazz);
        }
    }

    private void loadPackageMappings(String packageName) {
        throw new PandaRuntimeException("Not implemented");
    }

    public void loadClasses(Collection<Class<?>> classes) {
        loadedClasses.addAll(classes);
    }

    public void loadClass(Class<?> clazz) {
        loadedClasses.add(clazz);
    }

    public Collection<Class<?>> loadedClasses() {
        return loadedClasses;
    }

    public Collection<ClassPrototype> generate(ModuleRegistry registry) {
        long currentTime = System.nanoTime();

        Collection<Class<?>> queuedClasses = new ArrayList<>(loadedClasses);
        loadedClasses.clear();

        ClassPrototypeMappingGenerator generator = new ClassPrototypeMappingGenerator(this);
        Collection<ClassPrototype> prototypes = generator.generate(registry, queuedClasses);

        if (prototypes.size() != queuedClasses.size()) {
            throw new PandaRuntimeException("Something went wrong (sizeof queuedClass != sizeof generatedPrototypes");
        }

        totalLoadTime += System.nanoTime() - currentTime;
        return prototypes;
    }

    public long getTotalLoadTime() {
        return totalLoadTime;
    }

}
