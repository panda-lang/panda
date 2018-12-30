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

package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ProcessedAnnotation {

    protected final Class<? extends Annotation> annotationType;
    protected final Map<String, Object> values = new HashMap<>();

    public ProcessedAnnotation(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    public void load(Annotation annotation) throws Exception {
        for (Method declaredMethod : annotation.annotationType().getDeclaredMethods()) {
            values.put(declaredMethod.getName(), declaredMethod.invoke(annotation));
        }
    }

    public void load(String key, Object value) {
        values.put(key, value);
    }

    public <T> T getDefaultValue() {
        return getValue("value");
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        return (T) values.get(key);
    }

    public Class<? extends Annotation> getAnnotationType() {
        return annotationType;
    }

}
