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
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.module.ReferencesMap;
import org.panda_lang.framework.design.architecture.type.Referencable;
import org.panda_lang.framework.design.architecture.type.Reference;
import org.panda_lang.framework.language.architecture.type.array.ArrayClassTypeFetcher;
import org.panda_lang.framework.language.architecture.type.array.PandaArray;

import java.util.Collection;
import java.util.Map.Entry;

public class PandaModule extends PandaModules implements Module {

    protected final String name;
    protected final ModuleLoader loader;
    protected final ReferencesMap references;
    protected Module parent;

    public PandaModule(@Nullable Module parent, String name, ModuleLoader loader) {
        this.name = name;
        this.loader = loader;
        this.parent = parent;
        this.references = new PandaReferencesMap();
    }

    public PandaModule(String name, ModuleLoader loader) {
        this(null, name, loader);
    }

    @Override
    public Reference add(Referencable referencable) {
        Reference reference = referencable.toReference();
        references.put(reference);
        return reference;
    }

    @Override
    public int countUsedTypes() {
        return references.countUsedTypes();
    }

    @Override
    public int countTypes() {
        return references.size();
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
    public Option<Reference> forClass(Class<?> associatedClass) {
        return references.forClass(associatedClass);
    }

    @Override
    public Option<Reference> forName(CharSequence typeName) {
        if (typeName.toString().endsWith(PandaArray.IDENTIFIER)) {
            return ArrayClassTypeFetcher.fetch(this, typeName.toString());
        }

        return references.forName(typeName);
    }

    @Override
    public Collection<Entry<String, Reference>> getTypes() {
        Collection<Entry<String, Reference>> entries = references.getTypes();

        for (Module submodule : getModules()) {
            entries.addAll(submodule.getTypes());
        }

        return entries;
    }

    @Override
    public ModuleLoader getModuleLoader() {
        return loader;
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
