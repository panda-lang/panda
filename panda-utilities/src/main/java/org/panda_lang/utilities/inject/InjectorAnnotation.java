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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Annotation data wrapper.
 * Stores data about the annotation in {@link org.panda_lang.utilities.inject.InjectorAnnotation.Metadata} container.
 *
 * @param <T> annotation type
 */
public final class InjectorAnnotation<T extends Annotation> {

    private final T annotation;
    private final Metadata<T> metadata;

    InjectorAnnotation(Metadata<T> metadata) {
        this(null, metadata);
    }

    InjectorAnnotation(T annotation) throws InvocationTargetException, IllegalAccessException {
        this(annotation, new Metadata<>(annotation.annotationType()));
        this.metadata.load(annotation);
    }

    private InjectorAnnotation(T annotation, Metadata<T> metadata) {
        this.annotation = annotation;
        this.metadata = metadata;
    }

    public boolean hasAnnotation() {
        return annotation != null;
    }

    public Metadata<T> getMetadata() {
        return metadata;
    }

    public T getAnnotation() {
        return annotation;
    }

    /**
     * Stores in key-value format data about the associated annotation
     *
     * @param <T>
     */
    public static final class Metadata<T extends Annotation> {

        private final Class<? extends Annotation> annotationType;
        private final Map<String, Object> values = new HashMap<>();

        public Metadata(Class<? extends Annotation> annotationType) {
            this.annotationType = annotationType;
        }

        private Metadata<T> load(T annotation) throws InvocationTargetException, IllegalAccessException {
            for (Method declaredMethod : annotation.annotationType().getDeclaredMethods()) {
                values.put(declaredMethod.getName(), declaredMethod.invoke(annotation));
            }

            return this;
        }

        protected Metadata<T> load(String key, Object value) {
            values.put(key, value);
            return this;
        }

        @SuppressWarnings("unchecked")
        public <V> V getValue(String key) {
            return (V) values.get(key);
        }

        public String getValue() {
            return getValue("value");
        }

        @SuppressWarnings("unchecked")
        public Class<T> getAnnotationType() {
            return (Class<T>) annotationType;
        }

    }

}
