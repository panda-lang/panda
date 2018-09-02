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

package org.panda_lang.panda.framework.language.interpreter.parser.pipeline;

import org.panda_lang.panda.PandaException;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;

import java.util.Collection;

public class ParserRegistrationLoader {

    public PipelineRegistry load(AnnotationsScannerProcess scannerProcess) {
        PandaPipelineRegistry registry = new PandaPipelineRegistry();

        try {
            loadPipelines(registry, scannerProcess);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return registry;
    }

    private void loadPipelines(PandaPipelineRegistry registry, AnnotationsScannerProcess scannerProcess) throws Exception {
        Collection<Class<?>> annotated = scannerProcess.createSelector().selectTypesAnnotatedWith(ParserRegistration.class);

        for (Class<?> clazz : annotated) {
            ParserRegistration parserRegistration = clazz.getAnnotation(ParserRegistration.class);

            UnifiedParser parser = createParserInstance(clazz, parserRegistration.parserClass());
            ParserHandler handler = createHandlerInstance(parser, parserRegistration.handlerClass());
            ParserRepresentation representation = new PandaParserRepresentation(parser, handler, parserRegistration.priority());

            for (String target : parserRegistration.target()) {
                ParserPipeline pipeline = registry.getOrCreate(target);
                pipeline.registerParserRepresentation(representation);
            }
        }
    }

    private UnifiedParser createParserInstance(Class<?> currentClass, Class<? extends UnifiedParser> parserClass) throws Exception {
        if (parserClass != UnifiedParser.class) {
            return parserClass.newInstance();
        }
        else if (UnifiedParser.class.isAssignableFrom(currentClass)) {
            return (UnifiedParser) currentClass.newInstance();
        }
        else {
            throw new PandaException("Cannot create parser instance (source: " + currentClass + ")");
        }
    }

    private ParserHandler createHandlerInstance(UnifiedParser currentParser, Class<? extends ParserHandler> handlerClass) throws Exception {
        if (handlerClass != ParserHandler.class) {
            return handlerClass.newInstance();
        }
        else if (ParserHandler.class.isAssignableFrom(currentParser.getClass())) {
            return (ParserHandler) currentParser;
        }
        else {
            throw new PandaException("Cannot create parser handler instance (source: " + currentParser.getClass() + ")");
        }
    }

}
