package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationPipeline;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.AutowiredParameters;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.ProcessedAnnotation;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Type;
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
                Object[] parameters = convertParameters(layer, data, pipeline.generation(), interceptorData, localData);
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

    private void invoke(Method autowiredMethod, Object... parameters) throws Throwable {
        // System.out.println(autowiredMethod);
        autowiredMethod.setAccessible(true);

        try {
            autowiredMethod.invoke(bootstrapParser.getBootstrap().getInstance(), parameters);
        } catch (Exception e) {
            throw e.getCause();
        }
    }

    private Object[] convertParameters(LayerMethod layerMethod, ParserData delegatedData, Generation generation, InterceptorData interceptorData, LocalData localData) throws Throwable {
        Method autowiredMethod = layerMethod.getMethod();
        Class<?>[] parameterTypes = autowiredMethod.getParameterTypes();

        Annotation[][] parameterAnnotations = autowiredMethod.getParameterAnnotations();
        ProcessedAnnotation[] processedAnnotations = new ProcessedAnnotation[parameterTypes.length];

        for (int i = 0; i < processedAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];

            if (annotations.length == 0) {
                continue;
            }

            ProcessedAnnotation processedAnnotation = new ProcessedAnnotation(annotations[0].annotationType());
            processedAnnotations[i] = processedAnnotation;
            processedAnnotation.load(annotations[0]);
        }

        if (layerMethod.hasAutowiredParameters()) {
            AutowiredParameters autowiredParameters = layerMethod.getAutowiredParameters();
            int index = autowiredParameters.detectTo();

            for (Type type : autowiredParameters.value()) {
                ProcessedAnnotation processedAnnotation = new ProcessedAnnotation(type.with());
                processedAnnotation.load("value", type.value());
                processedAnnotations[type.index() != -1 ? type.index() : index++] = processedAnnotation;
            }
        }

        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = ParserLayerGeneratorUtils.findParameter(parameterTypes[i], processedAnnotations[i], delegatedData, generation, interceptorData, localData);
        }

        return parameters;
    }


}
