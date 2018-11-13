package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Interceptor;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Redactor;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.mapping.AbyssPatternMapping;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Map;

class ParserLayerGeneratorUtils {

    protected static @Nullable Object findParameter(Class<?> type, Annotation[] annotations, ParserData data, Generation generation, InterceptorData interceptor, LocalData local) {
        if (type.isAssignableFrom(ParserData.class) && annotations.length == 0) {
            return data;
        }

        if (type.isAssignableFrom(Generation.class) && annotations.length == 0) {
            return generation;
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
            return findComponent((Component) annotation, type, data);
        }

        if (annotationType == Redactor.class) {
            return findRedacted((Redactor) annotation, interceptor);
        }

        if (annotationType == Local.class) {
            return findLocal((Local) annotation, type, local);
        }

        if (annotationType == Interceptor.class) {
            return interceptor.getValue(type);
        }

        return null;
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

    private static @Nullable Object findRedacted(Redactor redactorQualifier, InterceptorData interceptorData) {
        AbyssPatternMapping redactor = interceptorData.getValue(AbyssPatternMapping.class);

        if (redactor == null) {
            return null;
        }

        return redactor.get(redactorQualifier.value());
    }

}
