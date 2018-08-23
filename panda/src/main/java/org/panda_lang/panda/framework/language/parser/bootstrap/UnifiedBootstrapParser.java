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

package org.panda_lang.panda.framework.language.parser.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationType;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Interceptor;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Redactor;
import org.panda_lang.panda.framework.language.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.language.parser.bootstrap.layer.LayerMethod;
import org.panda_lang.panda.framework.language.parser.bootstrap.layer.LocalData;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class UnifiedBootstrapParser implements UnifiedParser {

    private final PandaParserBootstrap bootstrap;
    private final List<LayerMethod> layers;

    public UnifiedBootstrapParser(PandaParserBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.layers = bootstrap.getLayers();
    }

    @Override
    public boolean parse(ParserData data) {
        InterceptorData interceptorData = bootstrap.hasInterceptor() ? bootstrap.getInterceptor().handle(this, data) : new InterceptorData();
        LocalData localData = new LocalData();

        delegate(data.fork(), interceptorData, localData, 0);
        return true;
    }

    private void delegate(ParserData data, InterceptorData interceptorData, LocalData localData, int index) {
        CasualParserGeneration generation = data.getComponent(UniversalComponents.GENERATION);
        CasualParserGenerationLayer generationLayer = generation.getLayer(CasualParserGenerationType.LOWER);

        LayerMethod layer = layers.get(index);
        CasualParserGenerationCallback callback = callback(interceptorData, localData, layer, index + 1);

        switch (layer.getDelegation()) {
            case IMMEDIATELY:
                generation.getLayer(CasualParserGenerationType.HIGHER).delegateImmediately(callback, data);
                break;
            case BEFORE:
                generationLayer.delegateBefore(callback, data);
                break;
            case DEFAULT:
                generationLayer.delegate(callback, data);
                break;
            case AFTER:
                generationLayer.delegateAfter(callback, data);
                break;
        }
    }

    private CasualParserGenerationCallback callback(InterceptorData interceptorData, LocalData localData, LayerMethod layer, int nextIndex) {
        Method autowiredMethod = layer.getMethod();

        return (delegatedData, nextLayer1) -> {
            Class<?>[] parameterTypes = autowiredMethod.getParameterTypes();
            Annotation[][] parameterAnnotations = autowiredMethod.getParameterAnnotations();
            Object[] parameters = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                Object parameter = findParameter(parameterTypes[i], parameterAnnotations[i], delegatedData, interceptorData, localData);

                if (parameter == null) {
                    throw new ParserBootstrapException("Cannot find parameter: " + parameterTypes[i] + " of " + bootstrap.getName());
                }

                parameters[i] = parameter;
            }

            autowiredMethod.setAccessible(true);

            try {
                autowiredMethod.invoke(bootstrap.getInstance(), parameters);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            if (nextIndex < layers.size()) {
                delegate(delegatedData.fork(), interceptorData, localData, nextIndex);
            }
        };
    }

    private Object findParameter(Class<?> parameterType, Annotation[] annotations, ParserData data, InterceptorData interceptorData, LocalData localData) {
        if (parameterType.isAssignableFrom(ParserData.class)) {
            return data;
        }

        if (parameterType.isAssignableFrom(InterceptorData.class)) {
            return interceptorData;
        }

        if (parameterType.isAssignableFrom(LocalData.class)) {
            return localData;
        }

        if (annotations.length == 0 || annotations.length > 1) {
            return null;
        }

        Annotation annotation = annotations[0];
        Class<?> annotationType = annotation.annotationType();

        if (annotationType == Component.class) {
            for (Object component : data.getComponents()) {
                if (component == null) {
                    continue;
                }

                if (parameterType.isAssignableFrom(component.getClass())) {
                    return component;
                }
            }

            return null;
        }

        if (annotationType == Local.class) {
            return localData.getValue(parameterType);
        }

        if (annotationType == Interceptor.class) {
            return interceptorData.getValue(parameterType);
        }

        if (annotationType == Redactor.class) {
            AbyssRedactor redactor = interceptorData.getValue(AbyssRedactor.class);

            if (redactor == null) {
                return null;
            }

            return redactor.get(((Redactor) annotation).value());
        }

        return null;
    }

}
