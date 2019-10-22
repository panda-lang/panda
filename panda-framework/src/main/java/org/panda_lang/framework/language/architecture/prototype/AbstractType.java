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
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.architecture.prototype.State;
import org.panda_lang.framework.design.architecture.prototype.Type;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.language.architecture.prototype.array.ArrayClassPrototypeFetcher;

import java.util.ArrayList;
import java.util.Collection;

abstract class AbstractType extends AbstractProperty implements Type {

    protected final Module module;
    protected final Class<?> associated;
    protected final String type;
    protected final State state;
    protected final Collection<Reference> bases = new ArrayList<>(1);

    protected AbstractType(String name, Module module, SourceLocation location, Class<?> associated, String type, State state, Visibility visibility) {
        super(name, location, visibility);

        if (module == null) {
            throw new IllegalArgumentException("Prototype needs module");
        }

        if (associated == null) {
            throw new IllegalArgumentException("Prototype has to be associated with a java class");
        }

        if (type == null) {
            throw new IllegalArgumentException("Prototype requires defined type");
        }

        if (state == null) {
            throw new IllegalArgumentException("State of prototype is missing");
        }

        this.module = module;
        this.associated = associated;
        this.type = type;
        this.state = state;
    }

    @Override
    public void addBase(Reference baseReference) {
        bases.add(baseReference);
    }

    @Override
    public boolean isAssignableFrom(Type type) { // this (Panda Class | Java Class) isAssociatedWith
        if (type == null) {
            return true;
        }

        return type.equals(this)
                || PandaPrototypeUtils.isAssignableFrom(associated, type.getAssociatedClass())
                || PandaPrototypeUtils.hasCommonPrototypes(bases, type.getBases());
    }

    @Override
    public Reference toArray(ModuleLoader loader) {
        return ArrayClassPrototypeFetcher.getArrayOf(getModule(), this, 1);
    }

    @Override
    public Collection<Reference> getBases() {
        return bases;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Class<?> getAssociatedClass() {
        return associated;
    }

    @Override
    public Module getModule() {
        return module;
    }

}
