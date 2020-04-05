/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.utilities.inject;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.inject.annotations.Injectable;
import org.panda_lang.utilities.inject.annotations.Wired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Optional;

final class InjectorProcessor {

    private final Injector injector;

    InjectorProcessor(Injector injector) {
        this.injector = injector;
    }

    protected Object[] fetchValues(Executable executable) throws InjectorException, InvocationTargetException, IllegalAccessException {
        InjectorAnnotation<?>[] injectorAnnotations = mapAnnotations(executable);

        Parameter[] types = executable.getParameters();
        Object[] parameters = new Object[injectorAnnotations.length];

        for (int index = 0; index < parameters.length; index++) {
            parameters[index] = fetchValue(executable, injectorAnnotations[index], types[index]);
        }

        return parameters;
    }

    private InjectorAnnotation<?>[] mapAnnotations(Executable executable) throws InjectorException, InvocationTargetException, IllegalAccessException {
        Class<?>[] parameterTypes = executable.getParameterTypes();
        Parameter[] parameters = executable.getParameters();

        InjectorAnnotation<?>[] injectorAnnotations = new InjectorAnnotation[parameterTypes.length];

        if (executable.getAnnotation(Wired.class) != null) {
            Wired wired = executable.getAnnotation(Wired.class);

            for (Wired.Link link : wired.value()) {
                int index = ArrayUtils.getIndex(parameters, parameter -> parameter.getName().equals(link.parameter()));

                if (index == -1) {
                    throw new InjectorException("Unknown parameter '" + link.parameter() + "'");
                }

                InjectorAnnotation.Metadata<?> metadata = new InjectorAnnotation.Metadata<>(link.with());
                metadata.load("value", link.value());

                injectorAnnotations[index] = new InjectorAnnotation<>(metadata);
            }
        }

        Annotation[][] parameterAnnotations = executable.getParameterAnnotations();

        for (int index = 0; index < parameterAnnotations.length; index++) {
            for (Annotation annotation : parameterAnnotations[index]) {
                if (!annotation.annotationType().isAnnotationPresent(Injectable.class)) {
                    continue;
                }

                injectorAnnotations[index] = new InjectorAnnotation<>(annotation);
            }
        }

        return injectorAnnotations;
    }

    private Object fetchValue(Executable executable, @Nullable InjectorAnnotation<?> annotation, Parameter required) throws InjectorException {
        try {
            return fetchParameter(annotation, required);
        } catch (Exception e) {
            throw new InjectorException("Failed to fetch values for " + executable + ", " + e.getClass() + ": " + e.getMessage(), e);
        }
    }

    private @Nullable Object fetchParameter(InjectorAnnotation<?> annotation, Parameter required) throws Exception {
        InjectorResources resources = injector.getResources();

        if (annotation != null) {
            Optional<InjectorResourceBind<?, ? super Object>> bindValue = resources.getBind(annotation.getMetadata().getAnnotationType());

            if (!bindValue.isPresent()) {
                throw new InjectorException("Missing annotation bind for '" + annotation.getMetadata().getAnnotationType() + "' annotation");
            }

            InjectorResourceBind<?, ? super Object> bind = bindValue.get();

            if (InjectorAnnotation.class.isAssignableFrom(bind.getDataType())) {
                return bind.getValue(required, annotation);
            }

            return bind.getValue(required, annotation.getAnnotation());
        }

        Optional<InjectorResourceBind<?, ? super Object>> bindValue = resources.getBind(required.getType());

        if (!bindValue.isPresent()) {
            throw new InjectorException("Missing type bind for " + required + " parameter");
        }

        return bindValue.get().getValue(required, null);
    }

}
