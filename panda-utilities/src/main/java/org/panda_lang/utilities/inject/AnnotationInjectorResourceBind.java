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

final class AnnotationInjectorResourceBind<T extends Annotation> extends DefaultInjectorResourceBind<T, InjectorAnnotation<T>> {

    AnnotationInjectorResourceBind(Class<T> associatedType) {
        super(associatedType, InjectorAnnotation.class);
    }

    @Override
    public Object getValue(Class<?> expected, InjectorAnnotation<T> data) throws Exception {
        return super.getValue(expected, data);
    }

}
