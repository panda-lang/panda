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

package org.panda_lang.panda.utilities.inject;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

final class InjectorProcessor {

    private final Injector injector;

    InjectorProcessor(Injector injector) {
        this.injector = injector;
    }

    protected Object[] fetchValues(Executable executable) throws InvocationTargetException, IllegalAccessException {
        Class<?>[] parameterTypes = executable.getParameterTypes();

        Annotation[][] parameterAnnotations = executable.getParameterAnnotations();

        /*
        InjectorAnnotation[] processedAnnotations = new InjectorAnnotation[parameterTypes.length];

        for (int i = 0; i < processedAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];

            if (annotations.length == 0) {
                continue;
            }

            InjectorAnnotation processedAnnotation = new InjectorAnnotation(annotations[0].annotationType());
            processedAnnotations[i] = processedAnnotation;
            processedAnnotation.load(annotations[0]);
        }
        */

        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = fetchParameter(i, parameterTypes[i], parameterAnnotations[i]);
        }

        return parameters;
    }

    private @Nullable Object fetchParameter(int i, Class<?> type, Annotation[] annotations) {
        Optional<InjectorResourceBind<?>> bindValue = injector.getResources().getBind(type);

        if (!bindValue.isPresent()) {
            throw new NullPointerException("Missing bind");
        }

        try {
            return bindValue.get().getValue(type, annotations.length > 0 ? annotations[0] : null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
