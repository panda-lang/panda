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

import io.vavr.control.Option;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.ClassUtils;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.function.ThrowingQuadFunction;
import org.panda_lang.utilities.commons.function.ThrowingTriFunction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

final class DefaultInjectorResources implements InjectorResources {

    private final Option<InjectorResources> parent;
    private final Map<Class<?>, InjectorResourceBind<Annotation>> binds;
    private final Map<HandlerRecord, InjectorResourceHandler<Annotation, Object, ?>> handlers;
    private final Map<Executable, Annotation[][]> cachedAnnotations;

    DefaultInjectorResources(
        @Nullable InjectorResources parent,
        Map<Class<?>, InjectorResourceBind<Annotation>> resources,
        Map<HandlerRecord, InjectorResourceHandler<Annotation, Object, ?>> handlers,
        Map<Executable, Annotation[][]> cachedAnnotations
    ) {
        this.parent = Option.of(parent);
        this.binds = new HashMap<>(resources);
        this.handlers = new HashMap<>(handlers);
        this.cachedAnnotations = new HashMap<>(cachedAnnotations);
    }

    DefaultInjectorResources() {
        this(null, new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    private <A extends Annotation> InjectorResourceBind<A> with(InjectorResourceBind<A> bind) {
        binds.put(bind.getAssociatedType(), ObjectUtils.cast(bind));
        return bind;
    }

    @Override
    public InjectorResourceBind<Annotation> on(Class<?> type) {
        return with(new DefaultInjectorResourceBind<>(type));
    }

    @Override
    public <A extends Annotation> InjectorResourceBind<A> annotatedWith(Class<A> annotation) {
        return with(new DefaultInjectorResourceBind<>(annotation));
    }

    @Override
    public <V, R, E extends Exception> void processType(Class<V> associatedType, ThrowingTriFunction<Parameter, V, Object[], R, E> processor) {
        with(new HandlerRecord(associatedType, null), new DefaultInjectorResourceHandler<>(null, processor));
    }

    @Override
    public <A extends Annotation, V, R, E extends Exception> void processAnnotated(Class<A> annotationType, ThrowingQuadFunction<A, Parameter, V, Object[], R, E> processor) {
        with(new HandlerRecord(null, annotationType), new DefaultInjectorResourceHandler<>(annotationType, processor));
    }

    @Override
    public <A extends Annotation, V, R, E extends Exception> void processAnnotatedType(Class<A> annotationType, Class<V> type, ThrowingQuadFunction<A, Parameter, V, Object[], R, E> processor) {
        with(new HandlerRecord(type, annotationType), new DefaultInjectorResourceHandler<>(annotationType, processor));
    }

    private <A extends Annotation, V, R, E extends Exception> void with(HandlerRecord record, DefaultInjectorResourceHandler<A, V, R, E> handler) {
        handlers.put(record, ObjectUtils.cast(handler));
    }

    @Override
    public Annotation[] fetchAnnotations(Parameter parameter) {
        Annotation[][] parameterAnnotations = fetchAnnotations(parameter.getDeclaringExecutable());
        int index = ArrayUtils.indexOf(parameter.getDeclaringExecutable().getParameters(), parameter);
        return parameterAnnotations[index];
    }

    @Override
    public Annotation[][] fetchAnnotations(Executable executable) {
        Annotation[][] parameterAnnotations = cachedAnnotations.get(executable);

        if (parameterAnnotations == null) {
            parameterAnnotations = executable.getParameterAnnotations();
            cachedAnnotations.put(executable, parameterAnnotations);
        }

        return parameterAnnotations;
    }

    @Override
    public Collection<InjectorResourceHandler<Annotation, Object, ?>> getHandler(Parameter parameter) {
        Executable executable = parameter.getDeclaringExecutable();
        Annotation[] annotations = fetchAnnotations(parameter);

        Collection<InjectorResourceHandler<Annotation, Object, ?>> matched = new ArrayList<>(executable.getParameterCount());
        add(matched, new HandlerRecord(parameter.getType(), null));

        for (Annotation annotation : annotations) {
            add(matched, new HandlerRecord(parameter.getType(), null));
            add(matched, new HandlerRecord(parameter.getType(), annotation.annotationType()));
            add(matched, new HandlerRecord(null, annotation.annotationType()));
        }

        return matched;
    }

    private void add(Collection<InjectorResourceHandler<Annotation, Object, ?>> matched, HandlerRecord record) {
        InjectorResourceHandler<Annotation, Object, ?> handler = handlers.get(record);

        if (handler != null) {
            matched.add(handler);
        }
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Option<InjectorResourceBind<Annotation>> getBind(Class<?> requestedType) {
        Optional<InjectorResourceBind<Annotation>> mostRelated = ClassUtils.selectMostRelated(binds.keySet(), requestedType).map(binds::get);

        if (mostRelated.isPresent()) {
            return Option.ofOptional(mostRelated);
        }

        List<InjectorResourceBind> associated = binds.keySet().stream()
                .filter(requestedType::isAssignableFrom)
                .map(binds::get)
                .collect(Collectors.toList());

        if (associated.size() == 1) {
            return Option.of(associated.get(0));
        }

        return parent
                .map(parent -> parent.getBind(requestedType))
                .get();
    }

    @Override
    public InjectorResources fork() {
        return new DefaultInjectorResources(this, new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    @Override
    public InjectorResources duplicate() {
        return new DefaultInjectorResources(null, binds, handlers, cachedAnnotations);
    }

    private static final class HandlerRecord {

        private final Class<?> type;
        private Class<? extends Annotation> annotation;

        private HandlerRecord(@Nullable Class<?> type, @Nullable Class<? extends Annotation> annotation) {
            this.type = type;
            this.annotation = annotation;
        }

        private void setAnnotation(Class<? extends Annotation> annotation) {
            this.annotation = annotation;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            HandlerRecord that = (HandlerRecord) o;
            return Objects.equals(annotation, that.annotation) && Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            int result = annotation != null ? annotation.hashCode() : 0;
            result = 31 * result + (type != null ? type.hashCode() : 0);
            return result;
        }

    }

}
