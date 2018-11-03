package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationPipeline;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LayerMethod;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LocalData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class ParserLayerGenerator {

    private final UnifiedBootstrapParser bootstrapParser;

    ParserLayerGenerator(UnifiedBootstrapParser bootstrapParser) {
        this.bootstrapParser = bootstrapParser;
    }

    protected GenerationCallback callback(InterceptorData interceptorData, LocalData localData, LayerMethod layer, int nextOrder, boolean last) {
        Method autowiredMethod = layer.getMethod();

        return new GenerationCallback() {
            @Override
            public void call(GenerationPipeline pipeline, ParserData data) throws Throwable {
                Object[] parameters = convertParameters(autowiredMethod, data, pipeline.generation(), interceptorData, localData);
                invoke(autowiredMethod, parameters);

                if (last && (nextOrder - bootstrapParser.getIndex()) < bootstrapParser.getLayers().size()) {
                    // System.out.println("DELEGATE >> " + bootstrapParser.getLayers().get(nextOrder - bootstrapParser.getIndex()).getMethod());
                    bootstrapParser.delegate(data.fork(), pipeline.generation(), interceptorData, localData, nextOrder);
                }
            }

            @Override
            public String toString() {
                return "Autowired " + layer.getMethod().getName();
            }
        };
    }

    private void invoke(Method autowiredMethod, Object... parameters) throws Exception {
        // System.out.println(autowiredMethod);
        autowiredMethod.setAccessible(true);

        try {
            autowiredMethod.invoke(bootstrapParser.getBootstrap().getInstance(), parameters);
        } catch (Exception e) {
            if (e.getCause() instanceof PandaParserFailure) {
                throw (PandaParserFailure) e.getCause();
            }

            throw new Exception(e.getCause());
        }
    }

    private Object[] convertParameters(Method autowiredMethod, ParserData delegatedData, Generation generation, InterceptorData interceptorData, LocalData localData) {
        Annotation[][] parameterAnnotations = autowiredMethod.getParameterAnnotations();
        Class<?>[] parameterTypes = autowiredMethod.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Object parameter = ParserLayerGeneratorUtils.findParameter(parameterTypes[i], parameterAnnotations[i], delegatedData, generation, interceptorData, localData);

            if (parameter == null) {
                throw new ParserBootstrapException("Cannot find parameter: " + parameterTypes[i] + " of " + bootstrapParser.getBootstrap().getName());
            }

            parameters[i] = parameter;
        }

        return parameters;
    }


}
