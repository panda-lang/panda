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

package org.panda_lang.panda.language.architecture.module;

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;

import java.util.ArrayList;
import java.util.Collection;

public class PandaModule implements Module {

    protected final String name;
    protected final Collection<ClassPrototypeReference> references;

    public PandaModule(String name) {
        this.name = name;
        this.references = new ArrayList<>();
    }

    @Override
    public ClassPrototypeReference add(ClassPrototypeReference reference) {
        this.references.add(reference);
        return reference;
    }

    @Override
    public int getAmountOfReferences() {
        return references.size();
    }

    @Override
    public Iterable<ClassPrototypeReference> getReferences() {
        return references;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.getName() + "[" + references.size() + "]";
    }

}
