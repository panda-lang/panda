/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.architecture.module;

import io.vavr.control.Option;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.TypesMap;
import org.panda_lang.framework.design.architecture.type.Type;

import java.util.ArrayList;
import java.util.Collection;

public class PandaModule extends PandaModules implements Module {

    protected final String name;
    protected final TypesMap types;
    protected @Nullable Module parent;

    public PandaModule(Module parent, String name) {
        this.name = name;
        this.parent = parent;
        this.types = new PandaTypesMap();
    }

    public PandaModule(String name) {
        this(null, name);
    }

    @Override
    public Type add(Type type) {
        types.put(type);
        return type;
    }

    @Override
    public int countUsedTypes() {
        return types.countUsedTypes();
    }

    @Override
    public int countTypes() {
        return types.size();
    }

    @Override
    public boolean isSubmodule(Module module) {
        Option<Module> parentModule = module.getParent();

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
    public Option<Type> forClass(Class<?> associatedClass) {
        return types.forClass(associatedClass);
    }

    @Override
    public Option<Type> forName(CharSequence typeName) {
        return types.forName(typeName);
    }

    @Override
    public Collection<Type> getAllTypes() {
        Collection<Type> entries = new ArrayList<>(types.values());

        for (Module submodule : getModules()) {
            entries.addAll(submodule.getAllTypes());
        }

        return entries;
    }

    @Override
    public Collection<Type> getTypes() {
        return new ArrayList<>(types.values());
    }

    @Override
    public Option<Module> getParent() {
        return Option.of(parent);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return (parent != null ? parent.toString() + ":" : "") + name;
    }

}
