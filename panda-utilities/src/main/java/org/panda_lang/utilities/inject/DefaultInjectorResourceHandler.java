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
import org.panda_lang.utilities.commons.function.ThrowingQuadFunction;
import org.panda_lang.utilities.commons.function.ThrowingTriFunction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

final class DefaultInjectorResourceHandler<A extends Annotation, V, R, E extends Exception> implements InjectorResourceHandler<A, V, R> {

    private final ThrowingQuadFunction<A, Parameter, V, Object[], R, E> processor;
    private final Option<Class<A>> annotationType;

    public DefaultInjectorResourceHandler(@Nullable Class<A> annotation, ThrowingQuadFunction<A, Parameter, V, Object[], R, E> processor) {
        this.processor = processor;
        this.annotationType = Option.of(annotation);
    }

    public DefaultInjectorResourceHandler(@Nullable Class<A> annotation, ThrowingTriFunction<Parameter, V, Object[], R, E> processor) {
        this(annotation, (_annotation, parameter, value, injectorArgs) -> {
            return processor.apply(parameter, value, injectorArgs);
        });
    }

    @Override
    public R process(Parameter required, A annotation, V value, Object... injectorArgs) throws Exception {
        return processor.apply(annotation, required, value, injectorArgs);
    }

    @Override
    public Option<Class<A>> getAnnotation() {
        return annotationType;
    }

}
