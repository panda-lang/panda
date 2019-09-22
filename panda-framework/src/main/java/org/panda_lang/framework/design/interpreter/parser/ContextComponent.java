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

package org.panda_lang.framework.design.interpreter.parser;

import org.panda_lang.utilities.commons.collection.Component;

import java.util.HashMap;
import java.util.Map;

public class ContextComponent<T> extends Component<T> {

    private static final Map<String, ContextComponent<?>> COMPONENTS = new HashMap<>();

    private ContextComponent(String name, Class<T> type) {
        super(name, type);
    }

    @SuppressWarnings("unchecked")
    public static <T> ContextComponent<T> of(String name, Class<T> type) {
        return (ContextComponent<T>) ofComponents(COMPONENTS, name, () -> new ContextComponent<>(name, type));
    }

}
