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

package org.panda_lang.panda.framework.design.interpreter.pattern.gapped.mapping;

import org.panda_lang.panda.framework.design.interpreter.pattern.PatternMapping;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

import java.util.HashMap;
import java.util.Map;

public class GappedPatternMapping implements PatternMapping {

    private final GappedPatternMappingContent hollows;
    private final Map<String, Integer> map;

    public GappedPatternMapping(GappedPatternMappingContent hollows) {
        this.hollows = hollows;
        this.map = new HashMap<>();
    }

    public GappedPatternMapping map(String... names) {
        if (names.length != hollows.getGaps().size()) {
            throw new PandaParserException("Cannot map mismatched hollows");
        }

        for (int i = 0; i < names.length; i++) {
            this.map.put(names[i], i);
        }

        return this;
    }

    @Override
    public Snippet get(String name) {
        Integer index = this.map.get(name);

        if (index == null) {
            throw new PandaRuntimeException("Hollow '" + name + "' does not exist");
        }

        return this.hollows.getGap(index);
    }

}
