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

package org.panda_lang.panda.framework.design.architecture.prototype;

import org.panda_lang.panda.framework.design.architecture.module.Module;

import java.util.ArrayList;
import java.util.Collection;

public class AbstractClassPrototypeMetadata implements ClassPrototypeMetadata {

    protected final String name;
    protected final Module module;
    protected final Class<?> associated;
    protected final Collection<ClassPrototypeReference> extended;

    protected AbstractClassPrototypeMetadata(String name, Module module, Class<?> associated) {
        this.name = name;
        this.module = module;
        this.associated = associated;
        this.extended = new ArrayList<>(1);
    }

    @Override
    public ClassPrototypeMetadata addExtended(ClassPrototypeReference reference) {
        extended.add(reference);
        return this;
    }

    @Override
    public boolean isClassOf(String className) {
        if (this.getClassName().equals(className)) {
            return true;
        }

        return this.associated != null && this.associated.getSimpleName().equals(className);

    }

    @Override
    public boolean isAssignableFrom(ClassPrototypeMetadata prototype) { // this (Panda Class | Java Class) isAssociatedWith
        if (prototype == null) {
            return true;
        }

        return prototype.equals(this)
                || PandaClassPrototypeUtils.isAssignableFrom(associated, prototype.getAssociatedClass())
                || PandaClassPrototypeUtils.hasCommonPrototypes(extended, prototype.getExtended());
    }

    @Override
    public Collection<ClassPrototypeReference> getExtended() {
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
    public String getClassName() {
        return name;
    }

}
