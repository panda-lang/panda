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

package org.panda_lang.panda.language.interpreter.parser;

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.interpreter.parser.ContextParser;
import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.design.interpreter.parser.ParserRepresentation;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Handler;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PandaParserRepresentation;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PandaPipelinePath;
import org.panda_lang.panda.PandaException;
import org.panda_lang.utilities.annotations.AnnotationsScannerProcess;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.stream.Collectors;

public final class RegistrableParsersLoader {

    private final Logger logger;

    public RegistrableParsersLoader(Logger logger) {
        this.logger = logger;
    }

    public PipelinePath load(AnnotationsScannerProcess scannerProcess) {
        return load(new PandaPipelinePath(), scannerProcess);
    }

    public PipelinePath load(PipelinePath path, AnnotationsScannerProcess scannerProcess) {
        return loadPipelines(path, scannerProcess);
    }

    @SuppressWarnings("unchecked")
    private PipelinePath loadPipelines(PipelinePath path, AnnotationsScannerProcess scannerProcess) {
        logger.debug("");
        logger.debug("--- Loading pipelines ");

        Collection<Class<? extends Parser>> loaded = scannerProcess.createSelector()
                .selectTypesAnnotatedWith(RegistrableParser.class)
                .stream()
                .filter(clazz -> {
                    if (Parser.class.isAssignableFrom(clazz)) {
                        return true;
                    }

                    logger.error(clazz + " is annotated with ParserRegistration and does not implement Parser");
                    return true;
                })
                .map(clazz -> (Class<? extends Parser>) clazz)
                .collect(Collectors.toList());

        loadParsers(path, loaded);
        logger.debug("");

        return path;
    }

    public PipelinePath loadParsers(PipelinePath path, Collection<Class<? extends Parser>> parsers) {
        try {
            return loadParsersInternal(path, parsers);
        } catch (Exception e) {
            throw new PandaFrameworkException("Cannot load parsers: " + e.getMessage(), e);
        }
    }

    private PipelinePath loadParsersInternal(PipelinePath path, Collection<Class<? extends Parser>> parsers) throws InstantiationException, IllegalAccessException {
        for (Class<?> clazz : parsers) {
            RegistrableParser registrable = clazz.getAnnotation(RegistrableParser.class);

            if (registrable == null) {
                continue;
            }

            Parser parser = createParserInstance(clazz, registrable.parserClass());
            Handler handler = createHandlerInstance(parser, registrable.handlerClass());
            ParserRepresentation<Parser> representation = new PandaParserRepresentation<>(parser, handler, registrable.priority());

            for (String target : registrable.pipeline()) {
                PipelineComponent<Parser> component = PipelineComponent.get(target);

                if (component == null) {
                    logger.warn("Pipeline '" + target + "' does not exist or its component was not initialized");
                    continue;
                }

                if (!path.hasPipeline(component)) {
                    path.createPipeline(component);
                }

                path.getPipeline(component).register(representation);
            }
        }

        return path;
    }

    private ContextParser createParserInstance(Class<?> currentClass, Class<? extends ContextParser> parserClass) throws IllegalAccessException, InstantiationException {
        if (parserClass != ContextParser.class) {
            return parserClass.newInstance();
        }
        else if (ContextParser.class.isAssignableFrom(currentClass)) {
            return (ContextParser) currentClass.newInstance();
        }

        throw new PandaException("Cannot create parser instance (source: " + currentClass + ")");
    }

    private Handler createHandlerInstance(Parser currentParser, Class<? extends Handler> handlerClass) throws IllegalAccessException, InstantiationException {
        if (handlerClass != Handler.class) {
            return handlerClass.newInstance();
        }
        else if (Handler.class.isAssignableFrom(currentParser.getClass())) {
            return (Handler) currentParser;
        }

        throw new PandaException("Cannot create parser handler instance (source: " + currentParser.getClass() + ")");
    }

}
