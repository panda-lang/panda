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

package org.panda_lang.panda.framework.design.resource.parsers;

import org.panda_lang.panda.PandaException;
import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.PandaParserRepresentation;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.PandaPipelinePath;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;

import java.util.Collection;

public class ParserRegistrationLoader {

    public PipelinePath load(AnnotationsScannerProcess scannerProcess) {
        return load(new PandaPipelinePath(), scannerProcess);
    }

    public PipelinePath load(PipelinePath path, AnnotationsScannerProcess scannerProcess) {
        return loadPipelines(path, scannerProcess);
    }

    private PipelinePath loadPipelines(PipelinePath path, AnnotationsScannerProcess scannerProcess) {
        return loadParsers(path, new ParserRegistrationScannerLoader().load(scannerProcess));
    }

    public PipelinePath loadParsers(PipelinePath path, Collection<Class<? extends Parser>> parsers) {
        try {
            return loadParsersInternal(path, parsers);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load pipelines: " + e.getMessage(), e);
        }
    }

    private PipelinePath loadParsersInternal(PipelinePath path, Collection<Class<? extends Parser>> parsers) throws InstantiationException, IllegalAccessException {
        for (Class<?> clazz : parsers) {
            ParserRegistration parserRegistration = clazz.getAnnotation(ParserRegistration.class);

            Parser parser = createParserInstance(clazz, parserRegistration.parserClass());
            ParserHandler handler = createHandlerInstance(parser, parserRegistration.handlerClass());
            ParserRepresentation<Parser> representation = new PandaParserRepresentation<>(parser, handler, parserRegistration.priority());

            for (String target : parserRegistration.target()) {
                PipelineComponent<Parser> component = PipelineComponent.get(target);

                if (component == null) {
                    PandaFramework.getLogger().warn("Pipeline '" + target + "' does not exist or its component was not initialized");
                    continue;
                }

                if (!path.hasPipeline(component)) {
                    path.createPipeline(component);
                }

                path.getPipeline(component).registerParserRepresentation(representation);
            }
        }

        PandaFramework.getLogger().debug("Pipelines: (" + path.names().size() + ") " + path.names());
        return path;
    }

    private UnifiedParser createParserInstance(Class<?> currentClass, Class<? extends UnifiedParser> parserClass) throws IllegalAccessException, InstantiationException {
        if (parserClass != UnifiedParser.class) {
            return parserClass.newInstance();
        }
        else if (UnifiedParser.class.isAssignableFrom(currentClass)) {
            return (UnifiedParser) currentClass.newInstance();
        }

        throw new PandaException("Cannot create parser instance (source: " + currentClass + ")");
    }

    private ParserHandler createHandlerInstance(Parser currentParser, Class<? extends ParserHandler> handlerClass) throws IllegalAccessException, InstantiationException {
        if (handlerClass != ParserHandler.class) {
            return handlerClass.newInstance();
        }
        else if (ParserHandler.class.isAssignableFrom(currentParser.getClass())) {
            return (ParserHandler) currentParser;
        }

        throw new PandaException("Cannot create parser handler instance (source: " + currentParser.getClass() + ")");
    }

}
