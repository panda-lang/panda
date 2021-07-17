/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.architecture.module;

import org.panda_lang.framework.architecture.packages.Package;
import org.panda_lang.framework.architecture.type.Reference;
import org.panda_lang.utilities.commons.collection.Maps;
import panda.std.Option;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PandaModule implements Module {

    protected final String name;
    protected final Package pkg;
    protected final Map<String, Reference> types = new HashMap<>(32);

    public PandaModule(Package pkg, String name) {
        this.pkg = pkg;
        this.name = name;
    }

    @Override
    public Reference add(Reference reference) {
        return Maps.put(types, reference.getSimpleName(), reference);
    }

    public long countUsedTypes() {
        return types.size();
    }

    public long countTypes() {
        return types.size();
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
    public Package getPackage() {
        return pkg;
    }

    @Override
    public String getSimpleName() {
        return name;
    }

    @Override
    public String getName() {
        return pkg.getName() + "@" + getSimpleName();
    }

    @Override
    public String toString() {
        return getName();
    }

}
