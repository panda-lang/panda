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

import org.panda_lang.utilities.commons.function.ThrowingBiFunction;
import org.panda_lang.utilities.commons.function.ThrowingTriFunction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

final class DefaultInjectorResourceHandler<A extends Annotation, V, R, E extends Exception> implements InjectorResourceHandler<V, R> {

    private final ThrowingTriFunction<A, Parameter, V, R, E> processor;

    public DefaultInjectorResourceHandler(ThrowingTriFunction<A, Parameter, V, R, E> processor) {
        this.processor = processor;
    }

    public DefaultInjectorResourceHandler(ThrowingBiFunction<Parameter, V, R, E> processor) {
        this.processor = (annotation, parameter, value) -> {
            return processor.apply(parameter, value);
        };
    }

    @Override
    public R process(Parameter required, V data) throws Exception {
        return processor.apply(null, required, data);
    }


}
