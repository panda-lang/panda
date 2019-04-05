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

package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserFailure;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.AutowiredParameters;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.ProcessedAnnotation;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Type;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LayerMethod;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ParserLayerGenerator<T> {

    private final BootstrapCoreParser<T> bootstrapParser;

    ParserLayerGenerator(BootstrapCoreParser<T> bootstrapParser) {
        this.bootstrapParser = bootstrapParser;
    }

    protected GenerationCallback<T> callback(InterceptorData interceptorData, LocalData localData, LayerMethod layer, int nextOrder, boolean last) {
        Method autowiredMethod = layer.getMethod();

        return new GenerationCallback<T>() {
            @Override
            public @Nullable T call(GenerationPipeline pipeline, ParserData data) throws Throwable {
                Object[] parameters = convertParameters(layer, data, pipeline.generation(), interceptorData, localData);
                T result;

                try {
                    //noinspection unchecked
                    result = (T) invoke(autowiredMethod, parameters);
                }
                catch (ParserFailure failure) {
                    failure.getData().setComponent(UniversalComponents.SOURCE_STREAM, new PandaSourceStream(failure.getData().getComponent(BootstrapComponents.CURRENT_SOURCE)));
                    throw failure;
                }

                if (last && (nextOrder - bootstrapParser.getIndex()) < bootstrapParser.getLayers().size()) {
                    bootstrapParser.delegate(data.fork(), pipeline.generation(), interceptorData, localData, nextOrder);
                }

                return result;
            }

            @Override
            public String toString() {
                return "Autowired " + layer.getMethod().getName();
            }
        };
    }

    private @Nullable Object invoke(Method autowiredMethod, Object... parameters) throws Throwable {
        autowiredMethod.setAccessible(true);

        try {
            return autowiredMethod.invoke(bootstrapParser.getBootstrap().getInstance(), parameters);
        }
        catch (IllegalArgumentException e) {
            PandaFramework.getLogger().warn(autowiredMethod.getName() + " may contains invalid annotations (" + autowiredMethod.getDeclaringClass() + ":" + autowiredMethod.getName() + ")");
            throw e;
        }
        catch (InvocationTargetException e) {
            throw e.getTargetException();
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
            int index = autowiredParameters.skip();
            int to = autowiredParameters.to();

            for (Type type : autowiredParameters.value()) {
                ProcessedAnnotation processedAnnotation = new ProcessedAnnotation(type.with());
                processedAnnotation.load("value", type.value());
                processedAnnotations[type.index() != -1 ? type.index() : index++] = processedAnnotation;

                if (to != -1 && to <= index) {
                    break;
                }
            }
        }

        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = ParserLayerGeneratorUtils.findParameter(i, parameterTypes[i], processedAnnotations[i], delegatedData, generation, interceptorData, localData);
        }

        return parameters;
    }


}
