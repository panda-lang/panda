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

import com.esotericsoftware.reflectasm.ConstructorAccess;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.PandaParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;

public class ParserRegistrationLoader {

    public PipelineRegistry load(Class<?> locationClass) {
        PandaPipelineRegistry registry = new PandaPipelineRegistry();

        try {
            loadPipelines(registry, locationClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return registry;
    }

    public PipelineRegistry load(PandaPipelineRegistry parserPipelineRegistry, Class<?> locationClass) {
        PandaPipelineRegistry registry = new PandaPipelineRegistry();

        try {
            loadPipelines(parserPipelineRegistry, locationClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return registry;
    }

    public void loadPipelines(PandaPipelineRegistry registry, Class<?> locationClass) throws Exception {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setClassLoaders(new ClassLoader[]{ locationClass.getClassLoader() });
        configurationBuilder.addUrls(locationClass.getProtectionDomain().getCodeSource().getLocation().toURI().toURL());

        Reflections reflections = new Reflections(configurationBuilder);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ParserRegistration.class);

        for (Class<?> clazz : annotated) {
            ParserRegistration parserRegistration = clazz.getAnnotation(ParserRegistration.class);

            ConstructorAccess<? extends UnifiedParser> parserConstructor = ConstructorAccess.get(parserRegistration.parserClass());
            UnifiedParser parser = parserConstructor.newInstance();

            ConstructorAccess<? extends ParserHandler> handlerConstructor = ConstructorAccess.get(parserRegistration.handlerClass());
            ParserHandler handler = handlerConstructor.newInstance();

            ParserRepresentation representation = new PandaParserRepresentation(parser, handler, parserRegistration.priority());

            for (String target : parserRegistration.target()) {
                ParserPipeline pipeline = registry.getOrCreate(target);
                pipeline.registerParserRepresentation(representation);
            }
        }
    }


}
