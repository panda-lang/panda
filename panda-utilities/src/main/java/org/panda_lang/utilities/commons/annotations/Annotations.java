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

package org.panda_lang.utilities.commons.annotations;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Annotations {

    private final Map<Class<? extends Annotation>, Annotation> annotationMap;

    public Annotations(Annotation[] annotations) {
        this.annotationMap = new HashMap<>(annotations.length);
        this.addAnnotations(annotations);
    }

    public void addAnnotations(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            annotationMap.put(annotation.annotationType(), annotation);
        }
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> Optional<A> getAnnotation(Class<A> annotationClass) {
        return Optional.ofNullable((A) annotationMap.get(annotationClass));
    }

}
