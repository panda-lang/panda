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

package org.panda_lang.utilities.commons.collection;

import java.util.Map;
import java.util.function.Supplier;

public abstract class Component<R> {

    private final String name;
    private final Class<R> type;

    protected Component(String name, Class<R> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<R> getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + "::" + type.getSimpleName();
    }

    protected static <TYPE> TYPE ofComponents(Map<String, TYPE> components, String name, Supplier<TYPE> supplier) {
        TYPE existingComponent = components.get(name);

        if (existingComponent != null) {
            throw new RuntimeException("Component '" + name + "' already exists (type: " + ((Component) existingComponent).getType() + ")");
        }

        TYPE component = supplier.get();
        components.put(name, component);

        return component;
    }

}
