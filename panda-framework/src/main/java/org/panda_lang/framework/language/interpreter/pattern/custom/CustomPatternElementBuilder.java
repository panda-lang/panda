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

package org.panda_lang.framework.language.interpreter.pattern.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public abstract class CustomPatternElementBuilder<T, B extends Buildable<T>> implements Buildable<T> {

    protected final String id;
    protected boolean optional;
    protected CustomReader<? extends T> reader;
    protected Collection<CustomVerify<? extends T>> verifies = new ArrayList<>(1);
    protected List<Function<?, ?>> mappers = new ArrayList<>(1);

    protected CustomPatternElementBuilder(String id) {
        this.id = id;
    }

    @Override
    public CustomPatternElement build() {
        return new CustomPatternElement(this);
    }

    public B optional() {
        this.optional = true;
        return returnThis();
    }

    public B custom(CustomReader<T> reader) {
        this.reader = reader;
        return returnThis();
    }

    public B verify(CustomVerify<? extends T> verify) {
        this.verifies.add(verify);
        return returnThis();
    }

    @SuppressWarnings("unchecked")
    public <V> CustomPatternElementBuilder<V, ? extends Buildable<V>> map(Function<T, V> mapper) {
        mappers.add(mapper);
        return (CustomPatternElementBuilder<V, ? extends Buildable<V>>) this;
    }

    @SuppressWarnings("unchecked")
    private B returnThis() {
        return (B) this;
    }

}
