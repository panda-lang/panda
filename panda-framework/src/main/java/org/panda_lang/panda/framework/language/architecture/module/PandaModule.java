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

package org.panda_lang.panda.framework.language.architecture.module;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;

import java.util.ArrayList;
import java.util.Collection;

public class PandaModule implements Module {

    private final String name;
    private final Collection<ClassPrototype> prototypes;

    public PandaModule(String name) {
        this.name = name;
        this.prototypes = new ArrayList<>();
    }

    @Override
    public ClassPrototype add(ClassPrototype prototype) {
        this.prototypes.add(prototype);
        return prototype;
    }

    @Override
    public @Nullable ClassPrototype get(String className) {
        for (ClassPrototype prototype : prototypes) {
            if (prototype.isClassOf(className)) {
                return prototype;
            }
        }

        return null;
    }

    @Override
    public int getAmountOfPrototypes() {
        return prototypes.size();
    }

    @Override
    public Collection<ClassPrototype> getPrototypes() {
        return prototypes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.getName() + "[" + prototypes.size() + "]";
    }

}
