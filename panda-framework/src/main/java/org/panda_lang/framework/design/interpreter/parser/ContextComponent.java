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

package org.panda_lang.framework.design.interpreter.parser;

import org.panda_lang.utilities.commons.collection.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents components used in the {@link org.panda_lang.framework.design.interpreter.parser.Context}
 *
 * @param <T> type of stored value
 */
public final class ContextComponent<T> extends Component<T> {

    private static final Map<String, ContextComponent<?>> COMPONENTS = new HashMap<>();

    private ContextComponent(String name, Class<T> type, double priority) {
        super(name, type, priority);
    }

    /**
     * Create component
     *
     * @param name name of component
     * @param type type of component
     * @param <T> generic type of component
     * @return a new component
     */
    public static <T> ContextComponent<T> of(String name, Class<T> type) {
        return of(name, type, 1.0);
    }

    /**
     * Create component with a custom priority
     *
     * @param name name of component
     * @param type type of component
     * @param priority the priority
     * @param <T> generic type of component
     * @return a new component
     */
    @SuppressWarnings("unchecked")
    public static <T> ContextComponent<T> of(String name, Class<T> type, double priority) {
        return (ContextComponent<T>) ofComponents(COMPONENTS, name, () -> new ContextComponent<>(name, type, priority));
    }

}
