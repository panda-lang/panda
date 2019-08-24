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

package org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.annotations.Inter;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.annotations.Local;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.data.InterceptorData;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.data.LocalData;
import org.panda_lang.panda.framework.design.interpreter.pattern.PatternMapping;
import org.panda_lang.panda.utilities.commons.StringUtils;
import org.panda_lang.panda.utilities.inject.InjectorAnnotation;
import org.panda_lang.panda.utilities.inject.InjectorController;
import org.panda_lang.panda.utilities.inject.InjectorResources;

import java.util.Map;
import java.util.Objects;

final class BootstrapInjectorController implements InjectorController {

    private Context context;
    private InterceptorData interceptorData;
    private LocalData localData;

    BootstrapInjectorController(Context context, InterceptorData interceptorData, LocalData localData) {
        this.context = context;
        this.interceptorData = interceptorData;
        this.localData = localData;
    }

    @Override
    public void initialize(InjectorResources resources) {
        resources.on(Context.class).assignInstance(() -> context);
        resources.on(InterceptorData.class).assignInstance(() -> interceptorData);
        resources.on(LocalData.class).assignInstance(() -> localData);

        resources.annotatedWithMetadata(Component.class).assignHandler((type, annotation) -> {
            return findComponent(annotation, type);
        });

        resources.annotatedWithMetadata(Src.class).assignHandler((type, annotation) -> {
            return findSource(annotation, type);
        });

        resources.annotatedWithMetadata(Local.class).assignHandler((type, annotation) -> {
            return findLocal(annotation, type);
        });

        resources.annotatedWithMetadata(Inter.class).assignHandler((type, annotation) -> {
            return interceptorData.getValue(type);
        });
    }

    private @Nullable Object findComponent(InjectorAnnotation<?> annotation, Class<?> type) {
        return context.getComponents().entrySet().stream()
                .filter(entry -> {
                    String value = annotation.getMetadata().getValue();

                    if (!StringUtils.isEmpty(value) && Objects.equals(entry.getKey().getName(), value)) {
                        return true;
                    }

                    return type == entry.getKey().getType();
                })
                .map(Map.Entry::getValue)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private @Nullable Object findSource(InjectorAnnotation<?> annotation, Class<?> requiredType) {
        PatternMapping redactor = interceptorData.getValue(PatternMapping.class);

        if (redactor == null) {
            return new BootstrapException("Pattern mappings are not defined for @Redactor");
        }

        Object value = redactor.get(annotation.getMetadata().getValue());

        if (value != null && requiredType == String.class) {
            return value.toString();
        }

        if (value != null && !requiredType.isAssignableFrom(value.getClass())) {
            throw new BootstrapException("Cannot match types: " + requiredType + " != " + value.getClass());
        }

        return value;
    }

    private @Nullable Object findLocal(InjectorAnnotation<?> annotation, Class<?> type) {
        String name = annotation.getMetadata().getValue();

        if (!StringUtils.isEmpty(name)) {
            return localData.getValue(name);
        }

        return localData.getValue(type);
    }

}
