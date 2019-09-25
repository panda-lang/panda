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

package org.panda_lang.framework.language.architecture.prototype;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMetadata;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.language.architecture.prototype.array.ArrayClassPrototypeFetcher;

import java.util.ArrayList;
import java.util.Collection;

abstract class AbstractPrototypeMetadata implements PrototypeMetadata {

    protected final String name;
    protected final Module module;
    protected final Class<?> associated;
    protected final Collection<PrototypeReference> extended;

    protected AbstractPrototypeMetadata(String name, Module module, Class<?> associated) {
        this.name = name;
        this.module = module;
        this.associated = associated;
        this.extended = new ArrayList<>(1);
    }

    @Override
    public PrototypeMetadata addExtended(PrototypeReference reference) {
        extended.add(reference);
        return this;
    }

    @Override
    public boolean isClassOf(String className) {
        if (this.getName().equals(className)) {
            return true;
        }

        return this.associated != null && this.associated.getSimpleName().equals(className);
    }

    @Override
    public boolean isAssignableFrom(PrototypeMetadata prototype) { // this (Panda Class | Java Class) isAssociatedWith
        if (prototype == null) {
            return true;
        }

        return prototype.equals(this)
                || PandaClassPrototypeUtils.isAssignableFrom(associated, prototype.getAssociatedClass())
                || PandaClassPrototypeUtils.hasCommonPrototypes(extended, prototype.getExtended());
    }

    @Override
    public PrototypeReference toArray(ModuleLoader loader) {
        return ArrayClassPrototypeFetcher.getArrayOf(getModule(), this, 1);
    }

    @Override
    public Collection<PrototypeReference> getExtended() {
        return extended;
    }

    @Override
    public Class<?> getAssociatedClass() {
        return associated;
    }

    @Override
    public Module getModule() {
        return module;
    }

    @Override
    public String getName() {
        return name;
    }

}
