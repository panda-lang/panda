/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.architecture.module;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.type.Reference;
import org.panda_lang.utilities.commons.function.Option;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PandaModule extends PandaModuleContainer implements Module {

    protected final String name;
    protected final Map<String, Reference> types = new HashMap<>(32);
    protected final Option<Module> parent;

    public PandaModule(@Nullable Module parent, String name) {
        this.name = name;
        this.parent = Option.of(parent);
    }

    public PandaModule(String name) {
        this(null, name);
    }

    @Override
    public boolean equals(Object to) {
        if (this == to) {
            return true;
        }

        if (to == null || getClass() != to.getClass()) {
            return false;
        }

        PandaModule that = (PandaModule) to;

        if (!name.equals(that.name)) {
            return false;
        }

        return parent.equals(that.parent);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + parent.hashCode();
        return result;
    }

    @Override
    public Reference add(Reference reference) {
        types.put(reference.getSimpleName(), reference);
        return reference;
    }

    @Override
    public long countUsedTypes() {
        return types.size();
    }

    @Override
    public long countTypes() {
        return types.size();
    }

    @Override
    public boolean hasSubmodule(Module module) {
        Option<? extends Module> parentModule = module.getParent();

        while (parentModule.isDefined()) {
            Module parent = parentModule.get();

            if (parent.equals(this)) {
                return true;
            }

            parentModule = parent.getParent();
        }

        return false;
    }

    @Override
    public Option<Reference> get(String typeName) {
        return Option.of(types.get(typeName));
    }

    @Override
    public Collection<? extends Reference> getReferences() {
        return new ArrayList<>(types.values());
    }

    @Override
    public Option<Module> getParent() {
        return parent;
    }

    @Override
    public String getSimpleName() {
        return name;
    }

    @Override
    public String getName() {
        return parent
                .map(Module::getName)
                .map(name -> name + ":")
                .orElseGet("")
                + getSimpleName();
    }

    @Override
    public String toString() {
        return getName();
    }

}
