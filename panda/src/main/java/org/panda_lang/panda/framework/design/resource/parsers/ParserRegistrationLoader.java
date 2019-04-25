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
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PandaParserRepresentation;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.PandaPipelinePath;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;

import java.util.Collection;

public class ParserRegistrationLoader {

    public PipelinePath load(AnnotationsScannerProcess scannerProcess) {
        PandaPipelinePath registry = new PandaPipelinePath();

        try {
            loadPipelines(registry, scannerProcess);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return registry;
    }

    @SuppressWarnings("unchecked")
    private void loadPipelines(PandaPipelinePath registry, AnnotationsScannerProcess scannerProcess) throws Exception {
        PandaFramework.getLogger().debug("");
        PandaFramework.getLogger().debug("--- Loading pipelines ");

        Collection<Class<?>> annotated = scannerProcess.createSelector().selectTypesAnnotatedWith(ParserRegistration.class);

        for (Class<?> clazz : annotated) {
            ParserRegistration parserRegistration = clazz.getAnnotation(ParserRegistration.class);

            Parser parser = createParserInstance(clazz, parserRegistration.parserClass());
            ParserHandler handler = createHandlerInstance(parser, parserRegistration.handlerClass());
            ParserRepresentation<Parser> representation = new PandaParserRepresentation<>(parser, handler, parserRegistration.priority());

            for (String target : parserRegistration.target()) {
                PipelineComponent<Parser> component = (PipelineComponent<Parser>) PipelineComponent.get(target);

                if (!registry.hasPipeline(component)) {
                    registry.createPipeline(component);
                }

                registry.getPipeline(component).registerParserRepresentation(representation);
            }
        }

        PandaFramework.getLogger().debug("Pipelines: (" + registry.names().size() + ") " + registry.names());
    }

    private UnifiedParser createParserInstance(Class<?> currentClass, Class<? extends UnifiedParser> parserClass) throws Exception {
        if (parserClass != UnifiedParser.class) {
            return parserClass.newInstance();
        }
        else if (UnifiedParser.class.isAssignableFrom(currentClass)) {
            return (UnifiedParser) currentClass.newInstance();
        }

        throw new PandaException("Cannot create parser instance (source: " + currentClass + ")");
    }

    private ParserHandler createHandlerInstance(Parser currentParser, Class<? extends ParserHandler> handlerClass) throws Exception {
        if (handlerClass != ParserHandler.class) {
            return handlerClass.newInstance();
        }
        else if (ParserHandler.class.isAssignableFrom(currentParser.getClass())) {
            return (ParserHandler) currentParser;
        }

        throw new PandaException("Cannot create parser handler instance (source: " + currentParser.getClass() + ")");
    }

}
