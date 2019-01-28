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
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Interceptor;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.ProcessedAnnotation;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.design.interpreter.pattern.PatternMapping;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.Map;

class ParserLayerGeneratorUtils {

    protected static @Nullable Object findParameter(int i, Class<?> type, ProcessedAnnotation annotation, ParserData data, Generation generation, InterceptorData interceptor, LocalData local) {
        if (annotation == null) {
            if (type.isAssignableFrom(ParserData.class)) {
                return data;
            }

            if (type.isAssignableFrom(Generation.class)) {
                return generation;
            }

            if (type.isAssignableFrom(InterceptorData.class)) {
                return interceptor;
            }

            if (type.isAssignableFrom(LocalData.class)) {
                return local;
            }

            if (type.isAssignableFrom(ExtractorResult.class)) {
                return interceptor.getValue(ExtractorResult.class);
            }

            throw new ParserBootstrapException("Unknown not annotated DI type: " + type.getName() + " (index: " + i + ")");
        }

        Class<?> annotationType = annotation.getAnnotationType();

        if (annotationType == Component.class) {
            return findComponent(annotation, type, data);
        }

        if (annotationType == Src.class) {
            return findRedacted(annotation, interceptor, type);
        }

        if (annotationType == Local.class) {
            return findLocal(annotation, type, local);
        }

        if (annotationType == Interceptor.class) {
            return interceptor.getValue(type);
        }

        throw new ParserBootstrapException("Unknown annotation: " + annotationType.getName());
    }

    private static @Nullable Object findComponent(ProcessedAnnotation componentQualifier, Class<?> type, ParserData data) {
        return data.getComponents().entrySet().stream()
                .filter(entry -> {
                    String value = componentQualifier.getDefaultValue();

                    if (!StringUtils.isEmpty(value) && entry.getKey().getName().equals(value)) {
                        return true;
                    }

                    return type == entry.getKey().getType();
                })
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private static @Nullable Object findLocal(ProcessedAnnotation localQualifier, Class<?> type, LocalData localData) {
        String name = localQualifier.getDefaultValue();

        if (!StringUtils.isEmpty(name)) {
            return localData.getValue(name);
        }

        return localData.getValue(type);
    }

    private static @Nullable Object findRedacted(ProcessedAnnotation srcQualifier, InterceptorData interceptorData, Class<?> requiredType) {
        PatternMapping redactor = interceptorData.getValue(PatternMapping.class);

        if (redactor == null) {
            return new ParserBootstrapException("Pattern mappings are not defined for @Redactor");
        }

        Tokens value = redactor.get(srcQualifier.getDefaultValue());

        if (value != null && requiredType == String.class) {
            return value.asString();
        }

        return value;
    }

}
