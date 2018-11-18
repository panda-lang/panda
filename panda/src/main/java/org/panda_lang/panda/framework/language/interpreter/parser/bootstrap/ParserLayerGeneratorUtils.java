package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Interceptor;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.mapping.PatternMapping;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.TokenExtractorResult;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Map;

class ParserLayerGeneratorUtils {

    protected static @Nullable Object findParameter(Class<?> type, Annotation[] annotations, ParserData data, Generation generation, InterceptorData interceptor, LocalData local) {
        if (annotations.length == 0) {
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

            if (type.isAssignableFrom(TokenExtractorResult.class)) {
                return interceptor.getValue(TokenExtractorResult.class);
            }
        }

        if (annotations.length == 0 || annotations.length > 1) {
            throw new ParserBootstrapException("Unknown not annotated DI type: " + type.getName());
        }

        Annotation annotation = annotations[0];
        Class<?> annotationType = annotation.annotationType();

        if (annotationType == Component.class) {
            return findComponent((Component) annotation, type, data);
        }

        if (annotationType == Src.class) {
            return findRedacted((Src) annotation, interceptor, type);
        }

        if (annotationType == Local.class) {
            return findLocal((Local) annotation, type, local);
        }

        if (annotationType == Interceptor.class) {
            return interceptor.getValue(type);
        }

        throw new ParserBootstrapException("Unknown annotation: " + annotationType.getName());
    }

    private static @Nullable Object findComponent(Component componentQualifier, Class<?> type, ParserData data) {
        return data.getComponents().entrySet().stream()
                .filter(entry -> {
                    if (!StringUtils.isEmpty(componentQualifier.name()) && entry.getKey().getName().equals(componentQualifier.name())) {
                        return true;
                    }

                    return type == entry.getKey().getType();
                })
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private static @Nullable Object findLocal(Local localQualifier, Class<?> type, LocalData localData) {
        String name = localQualifier.value();

        if (!StringUtils.isEmpty(name)) {
            return localData.getValue(name);
        }

        return localData.getValue(type);
    }

    private static @Nullable Object findRedacted(Src srcQualifier, InterceptorData interceptorData, Class<?> requiredType) {
        PatternMapping redactor = interceptorData.getValue(PatternMapping.class);

        if (redactor == null) {
            return new ParserBootstrapException("Pattern mappings are not defined for @Redactor");
        }

        Tokens value = redactor.get(srcQualifier.value());

        if (value != null && requiredType == String.class) {
            return value.asString();
        }

        return value;
    }

}
