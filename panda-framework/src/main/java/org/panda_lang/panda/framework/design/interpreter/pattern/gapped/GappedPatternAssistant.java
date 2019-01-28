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

package org.panda_lang.panda.framework.design.interpreter.pattern.gapped;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.mapping.GappedPatternMapping;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.mapping.GappedPatternMappingContent;

public class GappedPatternAssistant {

    public static @Nullable GappedPatternMapping traditionalMapping(GappedPattern pattern, ParserData info, String... mapping) {
        GappedPatternMappingContent hollows = GappedPatternAssistant.extract(pattern, info);

        if (hollows == null) {
            return null;
        }

        GappedPatternMapping redactor = new GappedPatternMapping(hollows);
        return redactor.map(mapping);
    }

    public static GappedPatternMappingContent extract(GappedPattern pattern, ParserData data) {
        return GappedPatternUtils.extract(pattern, data.getComponent(UniversalComponents.SOURCE_STREAM));
    }

}
