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
import java.lang.reflect.Executable;
import java.util.Collection;

final class InjectorCache {

    private final Annotation[] annotations;
    private final InjectorResourceBind<?, ? super Object>[] binds;
    private final Collection<InjectorResourceHandler<?, ?>>[] handlers;

    private InjectorCache(Annotation[] annotations, InjectorResourceBind<?, ? super Object>[] binds, Collection<InjectorResourceHandler<?, ?>>[] handlers) {
        this.annotations = annotations;
        this.binds = binds;
        this.handlers = handlers;
    }

    Annotation[] getAnnotations() {
        return annotations;
    }

    InjectorResourceBind<?, ? super Object>[] getBinds() {
        return binds;
    }

    Collection<InjectorResourceHandler<?, ?>>[] getHandlers() {
        return handlers;
    }

    public static InjectorCache of(InjectorProcessor processor, Executable executable) {
        Annotation[] annotations = processor.fetchAnnotations(executable);
        return new InjectorCache(annotations, processor.fetchBinds(annotations, executable), processor.fetchHandlers(executable));
    }

}
