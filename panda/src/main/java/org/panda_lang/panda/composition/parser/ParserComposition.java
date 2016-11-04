package org.panda_lang.panda.composition.parser;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserHandler;
import org.panda_lang.core.interpreter.parser.ParserPipeline;
import org.panda_lang.core.interpreter.parser.ParserRepresentation;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserPipeline;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserRepresentation;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;

public class ParserComposition {

    private final ParserPipeline pipeline;

    public ParserComposition() {
        this.pipeline = new PandaParserPipeline();

        try {
            this.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }

    protected void initialize() throws Exception {
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.setClassLoaders(new ClassLoader[]{ getClass().getClassLoader() });
        config.addUrls(Panda.class.getProtectionDomain().getCodeSource().getLocation().toURI().toURL());

        Reflections reflections = new Reflections(config);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ParserRegistration.class);

        for (Class<?> clazz : annotated) {
            ParserRegistration parserRegistration = clazz.getAnnotation(ParserRegistration.class);

            ConstructorAccess<? extends Parser> parserConstructor = ConstructorAccess.get(parserRegistration.parserClass());
            Parser parser = parserConstructor.newInstance();

            ConstructorAccess<? extends ParserHandler> handlerConstructor = ConstructorAccess.get(parserRegistration.handlerClass());
            ParserHandler handler = handlerConstructor.newInstance();

            ParserRepresentation representation = new PandaParserRepresentation(parser, handler);
            pipeline.registerParserRepresentation(representation);
        }
    }

    public ParserPipeline getPipeline() {
        return pipeline;
    }

}
