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
import java.util.Optional;

public interface InjectorResources {

    /**
     * Create bind for the specified type
     *
     * @param associatedType type to bind
     * @param <T> type
     * @return the bind based on associated type
     */
    <T> InjectorResourceBind<T> on(Class<T> associatedType);

    /**
     * Create bind for parameters annotated with the specified annotation
     *
     * @param annotation the annotation to bind
     * @param <T> type of annotation
     * @return the bind based on associate annotation
     */
    <T extends Annotation> InjectorResourceBind<T> annotatedWith(Class<T> annotation);

    /**
     * Get bind for the specified type or annotation
     *
     * @param associatedType the associated class with bind to search for
     * @return the wrapped bind
     */
    Optional<InjectorResourceBind<?>> getBind(Class<?> associatedType);

}
