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

import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Handler;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PandaParserRepresentation;
import org.panda_lang.panda.PandaException;
import org.panda_lang.utilities.commons.ObjectUtils;

import java.util.Collection;

public final class ParsersLoader {

    public PipelinePath loadParsers(PipelinePath path, Collection<Parser> parsers) {
        for (Parser parser : parsers) {
            if (!Handler.class.isAssignableFrom(parser.getClass())) {
                throw new PandaException("Cannot create parser handler instance (source: " + parser.getClass() + ")");
            }
            
            Handler handler = ObjectUtils.cast(parser);

            for (PipelineComponent<? extends Parser> pipeline : handler.pipeline()) {
                path.computeIfAbsent(pipeline).register(ObjectUtils.cast(new PandaParserRepresentation<>(parser, handler, handler.priority())));
            }
        }

        return path;
    }

}
