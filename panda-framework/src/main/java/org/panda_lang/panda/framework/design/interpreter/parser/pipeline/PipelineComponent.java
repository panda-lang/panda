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

package org.panda_lang.panda.framework.design.interpreter.parser.pipeline;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.AbstractComponent;

import java.util.HashMap;
import java.util.Map;

public class PipelineComponent<P extends Parser> extends AbstractComponent<P> {

    private static final Map<String, AbstractComponent> COMPONENTS = new HashMap<>();

    private PipelineComponent(String name, Class<P> type) {
        super(name, type);
    }

    public static <T extends Parser> PipelineComponent<T> of(String name, Class<T> type) {
        return ofComponents(COMPONENTS, name, () -> new PipelineComponent<>(name, type));
    }

    public static @Nullable PipelineComponent<? extends Parser> get(String name) {
        for (Map.Entry<String, AbstractComponent> entry : COMPONENTS.entrySet()) {
            if (entry.getKey().equals(name)) {
                return (PipelineComponent<?>) entry.getValue();
            }
        }

        return null;
    }

}
