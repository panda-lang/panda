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

package org.panda_lang.framework.design.interpreter.parser.pipeline;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.utilities.commons.collection.Component;

import java.util.HashMap;
import java.util.Map;

public class PipelineComponent<P extends Parser> extends Component<P> {

    private static final Map<String, PipelineComponent<? extends Parser>> COMPONENTS = new HashMap<>();

    private final Class<? extends PipelineComponents> container;

    private PipelineComponent(Class<? extends PipelineComponents> container, String name, Class<P> type) {
        super(name, type);
        this.container = container;
    }

    @Override
    public String toString() {
        return container.getSimpleName() + "::" + super.toString();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Parser> PipelineComponent<T> of(Class<? extends PipelineComponents> container, String name, Class<T> type) {
        return (PipelineComponent<T>) ofComponents(COMPONENTS, name, () -> new PipelineComponent<>(container, name, type));
    }

    @SuppressWarnings("unchecked")
    public static @Nullable PipelineComponent<Parser> get(String name) {
        for (Map.Entry<String, PipelineComponent<? extends Parser>> entry : COMPONENTS.entrySet()) {
            if (entry.getKey().equals(name)) {
                return (PipelineComponent<Parser>) entry.getValue();
            }
        }

        return null;
    }

}
