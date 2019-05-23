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

package org.panda_lang.panda.framework.design.interpreter.parser.component;

import java.util.HashMap;
import java.util.Map;

public class Component<T> extends AbstractComponent<T> {

    private static final Map<String, Component<?>> COMPONENTS = new HashMap<>();

    private Component(String name, Class<T> type) {
        super(name, type);
    }

    @SuppressWarnings("unchecked")
    public static <T> Component<T> of(String name, Class<T> type) {
        return (Component<T>) ofComponents(COMPONENTS, name, () -> new Component<T>(name, type));
    }

}
