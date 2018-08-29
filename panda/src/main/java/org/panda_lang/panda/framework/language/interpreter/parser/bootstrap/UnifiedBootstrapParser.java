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

package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Interceptor;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Redactor;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LayerMethod;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.utilities.commons.objects.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class UnifiedBootstrapParser implements UnifiedParser {

    private final PandaParserBootstrap bootstrap;
    private final List<LayerMethod> layers;

    public UnifiedBootstrapParser(PandaParserBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.layers = bootstrap.getLayers();
    }

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) {
        InterceptorData interceptorData = bootstrap.hasInterceptor() ? bootstrap.getInterceptor().handle(this, data) : new InterceptorData();
        LocalData localData = new LocalData();

        delegate(data, nextLayer, interceptorData, localData, 0);
        return true;
    }

    private void delegate(ParserData data, GenerationLayer nextLayer, InterceptorData interceptorData, LocalData localData, int index) {
        LayerMethod layer = layers.get(index);
        CasualParserGenerationCallback callback = callback(interceptorData, localData, layer, index + 1);

        switch (layer.getDelegation()) {
            case IMMEDIATELY:
                callback.call(data, nextLayer);
                break;
            case BEFORE:
                nextLayer.delegateBefore(callback, data);
                break;
            case DEFAULT:
                nextLayer.delegate(callback, data);
                break;
            case AFTER:
                nextLayer.delegateAfter(callback, data);
                break;
        }
    }

    private CasualParserGenerationCallback callback(InterceptorData interceptorData, LocalData localData, LayerMethod layer, int nextIndex) {
        Method autowiredMethod = layer.getMethod();

        return (delegatedData, nextLayer) -> {
            Class<?>[] parameterTypes = autowiredMethod.getParameterTypes();
            Annotation[][] parameterAnnotations = autowiredMethod.getParameterAnnotations();
            Object[] parameters = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                Object parameter = findParameter(parameterTypes[i], parameterAnnotations[i], delegatedData, nextLayer, interceptorData, localData);

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
                delegate(delegatedData.fork(), nextLayer, interceptorData, localData, nextIndex);
            }
        };
    }

    private @Nullable Object findParameter(Class<?> type, Annotation[] annotations, ParserData data, GenerationLayer layer, InterceptorData interceptor, LocalData local) {
        if (type.isAssignableFrom(ParserData.class) && annotations.length == 0) {
            return data;
        }

        if (type.isAssignableFrom(GenerationLayer.class) && annotations.length == 0) {
            return layer;
        }

        if (type.isAssignableFrom(InterceptorData.class) && annotations.length == 0) {
            return interceptor;
        }

        if (type.isAssignableFrom(LocalData.class) && annotations.length == 0) {
            return local;
        }

        if (annotations.length == 0 || annotations.length > 1) {
            return null;
        }

        Annotation annotation = annotations[0];
        Class<?> annotationType = annotation.annotationType();

        if (annotationType == Component.class) {
            Component componentQualifier = (Component) annotation;

            return data.getComponents().entrySet().stream()
                    .filter(entry -> {
                        if (!StringUtils.isEmpty(componentQualifier.name()) && entry.getKey().getName().equals(componentQualifier.name())) {
                            return true;
                        }

                        return type == entry.getKey().getType();
                    })
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .get();
        }

        if (annotationType == Local.class) {
            String name = ((Local) annotation).value();

            if (!StringUtils.isEmpty(name)) {
                return local.getValue(name);
            }

            return local.getValue(type);
        }

        if (annotationType == Interceptor.class) {
            return interceptor.getValue(type);
        }

        if (annotationType == Redactor.class) {
            AbyssRedactor redactor = interceptor.getValue(AbyssRedactor.class);

            if (redactor == null) {
                return null;
            }

            return redactor.get(((Redactor) annotation).value());
        }

        return null;
    }

}
