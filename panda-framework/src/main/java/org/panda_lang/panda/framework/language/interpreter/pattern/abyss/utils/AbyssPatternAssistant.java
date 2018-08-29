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

package org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPatternUtils;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactorHollows;

public class AbyssPatternAssistant {

    public static AbyssRedactor traditionalMapping(AbyssPattern pattern, ParserData info, String... mapping) {
        AbyssRedactorHollows hollows = AbyssPatternAssistant.extract(pattern, info);
        AbyssRedactor redactor = new AbyssRedactor(hollows);
        return redactor.map(mapping);
    }

    public static AbyssRedactorHollows extract(AbyssPattern pattern, ParserData data) {
        return AbyssPatternUtils.extract(pattern, data.getComponent(UniversalComponents.SOURCE_STREAM));
    }

}
