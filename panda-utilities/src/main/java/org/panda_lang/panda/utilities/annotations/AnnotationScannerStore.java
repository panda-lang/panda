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

package org.panda_lang.panda.utilities.annotations;

import javassist.bytecode.ClassFile;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.commons.collection.Multimap;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationScannerStore implements AnnotationsDisposable {

    private final Multimap<String, String> store;
    private final Map<String, ClassFile> classFiles;

    public AnnotationScannerStore() {
        this.store = new Multimap<>(new HashMap<>(), HashSet::new);
        this.classFiles = new HashMap<>();
    }

    @Override
    public void dispose() {
        store.clear();
        classFiles.clear();
    }

    public void addInheritors(String type, String inheritor) {
        store.put(type, inheritor);
    }

    public void addClassFile(ClassFile classFile) {
        classFiles.put(classFile.getName(), classFile);
    }

    public void addClassFiles(Collection<ClassFile> classFiles) {
        for (ClassFile classFile : classFiles) {
            addClassFile(classFile);
        }
    }

    public Set<String> getInheritorsOf(String type) {
        Set<String> inheritors = new HashSet<>();
        Collection<String> directInheritors = store.get(type);

        if (directInheritors == null) {
            return inheritors;
        }

        inheritors.addAll(directInheritors);

        for (String directInheritor : directInheritors) {
            Set<String> indirectInheritors = getInheritorsOf(directInheritor);
            inheritors.addAll(indirectInheritors);
        }

        return inheritors;
    }

    public @Nullable ClassFile getCachedClassFile(String type) {
        return classFiles.get(type);
    }

    public int getAmountOfCachedClassFiles() {
        return classFiles.size();
    }

    public Collection<? extends ClassFile> getCachedClassFiles() {
        return classFiles.values();
    }

}
