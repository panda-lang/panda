package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
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

    protected CasualParserGenerationCallback callback(InterceptorData interceptorData, LocalData localData, LayerMethod layer, int nextIndex, boolean last) {
        Method autowiredMethod = layer.getMethod();

        return (delegatedData, nextLayer) -> {
            Object[] parameters = convertParameters(autowiredMethod, delegatedData, nextLayer, interceptorData, localData);
            invoke(autowiredMethod, parameters);

            if (last && (nextIndex - bootstrapParser.getIndex()) < bootstrapParser.getLayers().size()) {
                bootstrapParser.delegate(delegatedData.fork(), nextLayer, interceptorData, localData, nextIndex);
            }
        };
    }

    private void invoke(Method autowiredMethod, Object... parameters) throws Exception {
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

    private Object[] convertParameters(Method autowiredMethod, ParserData delegatedData, GenerationLayer nextLayer, InterceptorData interceptorData, LocalData localData) {
        Annotation[][] parameterAnnotations = autowiredMethod.getParameterAnnotations();
        Class<?>[] parameterTypes = autowiredMethod.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Object parameter = ParserLayerGeneratorUtils.findParameter(parameterTypes[i], parameterAnnotations[i], delegatedData, nextLayer, interceptorData, localData);

            if (parameter == null) {
                throw new ParserBootstrapException("Cannot find parameter: " + parameterTypes[i] + " of " + bootstrapParser.getBootstrap().getName());
            }

            parameters[i] = parameter;
        }

        return parameters;
    }


}
