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

package org.panda_lang.framework.language.interpreter.parser.pipeline;

import org.panda_lang.framework.design.interpreter.parser.pipeline.Channel;

import java.util.HashMap;
import java.util.Map;

public final class PandaChannel implements Channel {

    private final Map<String, Object> map = new HashMap<>(1);

    @Override
    public Channel put(String identifier, Object value) {
        map.put(identifier, value);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String identifier, Class<T> type) {
        return (T) map.get(identifier);
    }

}
