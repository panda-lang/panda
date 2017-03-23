/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.implementation.interpreter.parser.pipeline.registry;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import org.panda_lang.panda.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.PandaParserRepresentation;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;

public class ParserRegistrationLoader {

    public PandaPipelineRegistry load() {
        PandaPipelineRegistry registry = new PandaPipelineRegistry();

        try {
            load(registry);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return registry;
    }

    protected void load(PandaPipelineRegistry registry) throws Exception {
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.setClassLoaders(new ClassLoader[]{ getClass().getClassLoader() });
        config.addUrls(Panda.class.getProtectionDomain().getCodeSource().getLocation().toURI().toURL());

        Reflections reflections = new Reflections(config);
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
