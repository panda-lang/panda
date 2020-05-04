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
import org.panda_lang.utilities.inject.annotations.Injectable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.Optional;

final class InjectorProcessor {

    private final Injector injector;

    InjectorProcessor(Injector injector) {
        this.injector = injector;
    }

    protected Object[] fetchValues(Executable executable) throws InjectorException {
        Annotation[] injectorAnnotations = mapAnnotations(executable);
        Parameter[] types = executable.getParameters();
        Object[] parameters = new Object[injectorAnnotations.length];

        for (int index = 0; index < parameters.length; index++) {
            parameters[index] = fetchValue(executable, injectorAnnotations[index], types[index]);
        }

        return parameters;
    }

    private Annotation[] mapAnnotations(Executable executable) {
        Class<?>[] parameterTypes = executable.getParameterTypes();
        Annotation[][] parameterAnnotations = executable.getParameterAnnotations();
        Annotation[] injectorAnnotations = new Annotation[parameterTypes.length];

        for (int index = 0; index < parameterAnnotations.length; index++) {
            for (Annotation annotation : parameterAnnotations[index]) {
                if (annotation.annotationType().isAnnotationPresent(Injectable.class)) {
                    injectorAnnotations[index] = annotation;
                }
            }
        }

        return injectorAnnotations;
    }

    private Object fetchValue(Executable executable, @Nullable Annotation annotation, Parameter required) throws InjectorException {
        try {
            return fetchParameter(annotation, required);
        } catch (Exception e) {
            throw new InjectorException("Failed to fetch values for " + executable + ", " + e.getClass() + ": " + e.getMessage(), e);
        }
    }

    private @Nullable Object fetchParameter(Annotation annotation, Parameter required) throws Exception {
        InjectorResources resources = injector.getResources();

        if (annotation != null) {
            Optional<InjectorResourceBind<?, ? super Object>> bindValue = resources.getBind(annotation.annotationType());

            if (!bindValue.isPresent()) {
                throw new InjectorException("Missing annotation bind for '" + annotation.annotationType() + "' annotation");
            }

            InjectorResourceBind<?, ? super Object> bind = bindValue.get();
            return bind.getValue(required, annotation);
        }

        Optional<InjectorResourceBind<?, ? super Object>> bindValue = resources.getBind(required.getType());

        if (!bindValue.isPresent()) {
            throw new InjectorException("Missing type bind for " + required + " parameter");
        }

        return bindValue.get().getValue(required, null);
    }

}
