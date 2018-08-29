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

package org.panda_lang.panda.framework.design.interpreter.parser.component;

import org.panda_lang.panda.framework.PandaFrameworkException;

import java.util.HashMap;
import java.util.Map;

public class Component<T> {

    private static final Map<String, Component<?>> components = new HashMap<>();

    private final String name;
    private final Class<T> type;

    private Component(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    public static <T> Component<T> of(String name, Class<T> type) {
        Component<?> existingComponent = components.get(name);

        if (existingComponent != null) {
            throw new PandaFrameworkException("Component '" + name + "' already exists (type: " + existingComponent.getType() + ")");
        }

        Component<T> component = new Component<>(name, type);
        components.put(name, component);

        return component;
    }

}
