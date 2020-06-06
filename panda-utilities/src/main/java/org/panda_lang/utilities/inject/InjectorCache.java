/*
 * Copyright (c) 2020 Dzikoysk
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
import java.util.Map;

final class InjectorCache {

    private final Annotation[] injectable;
    private final Map<Class<? extends Annotation>, Annotation>[] annotations;
    private final InjectorResourceBind<Annotation>[] binds;
    private final Collection<InjectorResourceHandler<Annotation, Object, ?>>[] handlers;

    private InjectorCache(
        Annotation[] injectable,
        Map<Class<? extends Annotation>, Annotation>[] annotations,
        InjectorResourceBind<Annotation>[] binds,
        Collection<InjectorResourceHandler<Annotation, Object, ?>>[] handlers
    ) {
        this.injectable = injectable;
        this.annotations = annotations;
        this.binds = binds;
        this.handlers = handlers;
    }

    Annotation[] getInjectable() {
        return injectable;
    }

    Map<Class<? extends Annotation>, Annotation>[] getAnnotations() {
        return annotations;
    }

    InjectorResourceBind<Annotation>[] getBinds() {
        return binds;
    }

    Collection<InjectorResourceHandler<Annotation, Object, ?>>[] getHandlers() {
        return handlers;
    }

    public static InjectorCache of(InjectorProcessor processor, Executable executable) {
        Annotation[] injectable = processor.fetchAnnotations(executable);

        return new InjectorCache(
                injectable,
                processor.fetchAnnotationsMap(executable),
                processor.fetchBinds(injectable, executable),
                processor.fetchHandlers(executable)
        );
    }

}
