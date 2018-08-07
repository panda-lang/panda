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

package org.panda_lang.panda.framework.design.interpreter.parser.pipeline;

import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.PandaParserRepresentation;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;

import java.util.Set;

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

    public void loadPipelines(PandaPipelineRegistry registry, AnnotationsScannerProcess scannerProcess) throws Exception {
        Set<Class<?>> annotated = scannerProcess.createSelector().selectTypesAnnotatedWith(ParserRegistration.class);

        for (Class<?> clazz : annotated) {
            ParserRegistration parserRegistration = clazz.getAnnotation(ParserRegistration.class);

            UnifiedParser parser = parserRegistration.parserClass().newInstance();
            ParserHandler handler = parserRegistration.handlerClass().newInstance();
            ParserRepresentation representation = new PandaParserRepresentation(parser, handler, parserRegistration.priority());

            for (String target : parserRegistration.target()) {
                ParserPipeline pipeline = registry.getOrCreate(target);
                pipeline.registerParserRepresentation(representation);
            }
        }
    }

}
