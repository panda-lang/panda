/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.structure.struct.group;

import org.panda_lang.panda.language.structure.struct.ClassPrototype;

import java.util.HashMap;
import java.util.Map;

public class Group {

    private final String name;
    private final Map<String, ClassPrototype> prototypes;

    public Group(String name) {
        this.name = name;
        this.prototypes = new HashMap<>();
    }

    public void add(ClassPrototype prototype) {
        this.prototypes.put(prototype.getClassName(), prototype);
    }

    public ClassPrototype get(String className) {
        return prototypes.get(className);
    }

    public Map<String, ClassPrototype> getPrototypes() {
        return prototypes;
    }

    public String getName() {
        return name;
    }

}
