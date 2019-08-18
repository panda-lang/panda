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

package org.panda_lang.panda.framework.design.interpreter.parser.loader;

import org.panda_lang.panda.PandaException;
import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.interpreter.parser.ContextParser;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.PandaParserRepresentation;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.PandaPipelinePath;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;

import java.util.Collection;
import java.util.stream.Collectors;

public class RegistrableLoader {

    public PipelinePath load(AnnotationsScannerProcess scannerProcess) {
        return load(new PandaPipelinePath(), scannerProcess);
    }

    public PipelinePath load(PipelinePath path, AnnotationsScannerProcess scannerProcess) {
        return loadPipelines(path, scannerProcess);
    }

    @SuppressWarnings("unchecked")
    private PipelinePath loadPipelines(PipelinePath path, AnnotationsScannerProcess scannerProcess) {
        PandaFramework.getLogger().debug("");
        PandaFramework.getLogger().debug("--- Loading pipelines ");

        Collection<Class<? extends Parser>> loaded = scannerProcess.createSelector()
                .selectTypesAnnotatedWith(Registrable.class)
                .stream()
                .filter(clazz -> {
                    if (Parser.class.isAssignableFrom(clazz)) {
                        return true;
                    }

                    PandaFramework.getLogger().error(clazz + " is annotated with ParserRegistration and does not implement Parser");
                    return true;
                })
                .map(clazz -> (Class<? extends Parser>) clazz)
                .collect(Collectors.toList());

        return loadParsers(path, loaded);
    }

    public PipelinePath loadParsers(PipelinePath path, Collection<Class<? extends Parser>> parsers) {
        try {
            return loadParsersInternal(path, parsers);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load parsers: " + e.getMessage(), e);
        }
    }

    private PipelinePath loadParsersInternal(PipelinePath path, Collection<Class<? extends Parser>> parsers) throws InstantiationException, IllegalAccessException {
        for (Class<?> clazz : parsers) {
            Registrable registrable = clazz.getAnnotation(Registrable.class);

            if (registrable == null) {
                continue;
            }

            Parser parser = createParserInstance(clazz, registrable.parserClass());
            ParserHandler handler = createHandlerInstance(parser, registrable.handlerClass());
            ParserRepresentation<Parser> representation = new PandaParserRepresentation<>(parser, handler, registrable.priority());

            for (String target : registrable.pipeline()) {
                PipelineComponent<Parser> component = PipelineComponent.get(target);

                if (component == null) {
                    PandaFramework.getLogger().warn("Pipeline '" + target + "' does not exist or its component was not initialized");
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
