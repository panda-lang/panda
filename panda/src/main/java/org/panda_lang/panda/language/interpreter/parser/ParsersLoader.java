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

package org.panda_lang.panda.language.interpreter.parser;

import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.pool.Target;
import org.panda_lang.language.interpreter.parser.pool.ParserPoolService;
import org.panda_lang.utilities.commons.ObjectUtils;

import java.util.Collection;

public final class ParsersLoader {

    public ParserPoolService loadParsers(ParserPoolService path, Collection<Parser> parsers) {
        for (Parser parser : parsers) {
            Handler handler = (Handler) parser;

            for (Target<? extends Parser> pipeline : handler.pipeline()) {
                path.computeIfAbsent(pipeline).register(ObjectUtils.cast(new PandaParserRepresentation<>(parser, handler, handler.priority())));
            }
        }

        return path;
    }

}
