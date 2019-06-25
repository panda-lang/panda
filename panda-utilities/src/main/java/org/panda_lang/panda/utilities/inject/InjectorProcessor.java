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
import java.util.Optional;

final class InjectorProcessor {

    private final Injector injector;

    InjectorProcessor(Injector injector) {
        this.injector = injector;
    }

    protected Object[] fetchValues(Executable executable) throws InjectorException {
        Class<?>[] parameterTypes = executable.getParameterTypes();

        Annotation[][] parameterAnnotations = executable.getParameterAnnotations();
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            try {
                parameters[i] = fetchParameter(parameterTypes[i], parameterAnnotations[i]);
            } catch (Exception e) {
                throw new InjectorException("Failed to fetch values for " + executable + ": " + e.getMessage(), e);
            }
        }

        return parameters;
    }

    private @Nullable Object fetchParameter(Class<?> type, Annotation[] annotations) throws Exception {
        InjectorResources resources = injector.getResources();

        for (Annotation annotation : annotations) {
            Optional<InjectorResourceBind<?>> bindValue = resources.getBind(annotation.annotationType());

            if (!bindValue.isPresent()) {
                continue;
            }

            return bindValue.get().getValue(type, annotation);
        }

        Optional<InjectorResourceBind<?>> bindValue = resources.getBind(type);

        if (!bindValue.isPresent()) {
            throw new InjectorException("Missing bind for " + type + " parameter");
        }

        return bindValue.get().getValue(type, annotations);
    }

}
