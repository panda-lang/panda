/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.interpreter.parser.pool;

import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.utilities.commons.collection.Component;
import org.panda_lang.utilities.commons.function.Option;

import java.util.HashMap;
import java.util.Map;

/**
 * Component that represents a pipeline
 *
 * @param <P> pipeline parser type
 */
public final class Target<P extends Parser> extends Component<P> {

    private static final Map<String, Target<? extends Parser>> COMPONENTS = new HashMap<>();

    private Target(String name, Class<P> type) {
        super(name, type, 0);
    }

    /**
     * Create component
     *
     * @param name the name of component
     * @param type the type of component
     * @param <T> generic type of component
     * @return a new component
     */
    @SuppressWarnings("unchecked")
    public static <T extends Parser> Target<T> of(String name, Class<T> type) {
        return (Target<T>) ofComponents(COMPONENTS, name, () -> new Target<>(name, type));
    }

    /**
     * Get component with the given name
     *
     * @param name the name to search for
     * @return a found component
     */
    @SuppressWarnings("unchecked")
    public static Option<Target<Parser>> get(String name) {
        for (Map.Entry<String, Target<? extends Parser>> entry : COMPONENTS.entrySet()) {
            if (entry.getKey().equals(name)) {
                return Option.of((Target<Parser>) entry.getValue());
            }
        }

        return Option.none();
    }

}
