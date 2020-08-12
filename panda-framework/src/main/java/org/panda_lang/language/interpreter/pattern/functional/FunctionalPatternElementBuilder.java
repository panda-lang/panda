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

package org.panda_lang.language.interpreter.pattern.functional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public abstract class FunctionalPatternElementBuilder<T, B extends Buildable<T>> implements Buildable<T> {

    protected final String id;
    protected boolean optional;
    protected Reader<? extends T> reader;
    protected final Collection<Verifier<? extends T>> verifies = new ArrayList<>(1);
    protected final List<Function<?, ?>> mappers = new ArrayList<>(1);

    protected FunctionalPatternElementBuilder(String id) {
        this.id = id;
    }

    @Override
    public Element<T> build() {
        return new Element<>(this);
    }

    public B optional() {
        this.optional = true;
        return returnThis();
    }

    public B reader(Reader<T> reader) {
        this.reader = reader;
        return returnThis();
    }

    public B verify(Verifier<? extends T> verifier) {
        this.verifies.add(verifier);
        return returnThis();
    }

    @SuppressWarnings("unchecked")
    public <V> FunctionalPatternElementBuilder<V, ? extends Buildable<V>> map(Function<T, V> mapper) {
        mappers.add(mapper);
        return (FunctionalPatternElementBuilder<V, ? extends Buildable<V>>) this;
    }

    @SuppressWarnings("unchecked")
    protected B returnThis() {
        return (B) this;
    }

}
