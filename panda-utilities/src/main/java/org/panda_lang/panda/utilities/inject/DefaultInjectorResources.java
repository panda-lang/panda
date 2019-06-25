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

import org.panda_lang.panda.utilities.commons.ClassUtils;
import org.panda_lang.panda.utilities.commons.collection.Maps;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class DefaultInjectorResources implements InjectorResources {

    private final Map<Class<?>, InjectorResourceBind<?>> binds = new HashMap<>();

    private <T> InjectorResourceBind<T> with(InjectorResourceBind<T> bind) {
        return Maps.put(binds, bind.getAssociatedType(), bind);
    }

    @Override
    public <T> InjectorResourceBind<T> on(Class<T> type) {
        return with(new DefaultInjectorResourceBind<>(type));
    }

    @Override
    public <T extends Annotation> InjectorResourceBind<T> annotatedWith(Class<T> annotation) {
        return with(new DefaultInjectorResourceBind<>(annotation));
    }

    @Override
    public Optional<InjectorResourceBind<?>> getBind(Class<?> associatedType) {
        return ClassUtils.selectMostRelated(binds.keySet(), associatedType).map(binds::get);
    }

}
