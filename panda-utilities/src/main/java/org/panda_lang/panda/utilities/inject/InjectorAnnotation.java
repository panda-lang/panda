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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class InjectorAnnotation {

    private final Class<? extends Annotation> annotationType;
    private final Map<String, Object> values = new HashMap<>();

    InjectorAnnotation(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    protected void load(Annotation annotation) throws InvocationTargetException, IllegalAccessException {
        for (Method declaredMethod : annotation.annotationType().getDeclaredMethods()) {
            values.put(declaredMethod.getName(), declaredMethod.invoke(annotation));
        }
    }

    protected void load(String key, Object value) {
        values.put(key, value);
    }

    @SuppressWarnings("unchecked")
    protected <T> T getValue(String key) {
        return (T) values.get(key);
    }

}
