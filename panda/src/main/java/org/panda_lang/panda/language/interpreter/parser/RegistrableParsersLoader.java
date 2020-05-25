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
import org.panda_lang.framework.design.interpreter.parser.ParserRepresentation;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Handler;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PandaParserRepresentation;
import org.panda_lang.panda.PandaException;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.slf4j.Logger;

import java.util.Collection;

public final class RegistrableParsersLoader {

    private final Logger logger;

    public RegistrableParsersLoader(Logger logger) {
        this.logger = logger;
    }

    public PipelinePath loadParsers(PipelinePath path, Collection<Parser> parsers) {
        for (Parser parser : parsers) {
            RegistrableParser registrable = parser.getClass().getAnnotation(RegistrableParser.class);

            if (registrable == null) {
                continue;
            }

            if (!Handler.class.isAssignableFrom(parser.getClass())) {
                throw new PandaException("Cannot create parser handler instance (source: " + parser.getClass() + ")");
            }
            
            Handler handler = ObjectUtils.cast(parser);
            ParserRepresentation<Parser> representation = new PandaParserRepresentation<>(parser, handler, registrable.priority());

            for (String target : registrable.pipeline()) {
                PipelineComponent.get(target)
                        .onEmpty(() -> logger.warn("Pipeline '" + target + "' does not exist or its component was not initialized"))
                        .peek(component -> {
                            if (!path.hasPipeline(component)) {
                                path.createPipeline(component);
                            }

                            path.getPipeline(component).register(representation);
                        });
            }
        }

        return path;
    }

}
