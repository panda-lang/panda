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

import org.panda_lang.utilities.commons.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

final class DefaultInjectorResources implements InjectorResources {

    private final Map<Class<?>, InjectorResourceBind> binds = new HashMap<>();

    private <T, V> InjectorResourceBind<T, V> with(InjectorResourceBind<T, V> bind) {
        binds.put(bind.getAssociatedType(), bind);
        return bind;
    }

    @Override
    public <T> InjectorResourceBind<T, T> on(Class<T> type) {
        return with(new DefaultInjectorResourceBind<>(type));
    }

    @Override
    public <T extends Annotation> InjectorResourceBind<T, T> annotatedWith(Class<T> annotation) {
        return with(new DefaultInjectorResourceBind<>(annotation));
    }

    @Override
    public <T extends Annotation> InjectorResourceBind<T, InjectorAnnotation<T>> annotatedWithMetadata(Class<T> annotation) {
        return with(new AnnotationInjectorResourceBind<>(annotation));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<InjectorResourceBind<?, ? super Object>> getBind(Class<?> requestedType) {
        Optional<InjectorResourceBind<?, ? super Object>> mostRelated = ClassUtils.selectMostRelated(binds.keySet(), requestedType).map(binds::get);

        if (mostRelated.isPresent()) {
            return mostRelated;
        }

        List<InjectorResourceBind> associated = binds.keySet().stream()
                .filter(requestedType::isAssignableFrom)
                .map(binds::get)
                .collect(Collectors.toList());

        if (associated.size() != 1) {
            return Optional.empty();
        }

        return Optional.ofNullable(associated.get(0));
    }

}
