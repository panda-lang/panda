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

package org.panda_lang.framework.design.interpreter.pattern.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class AbstractCustomPatternElement implements CustomPatternElement {

    private final String id;
    private final boolean optional;
    private final CustomReader<?> reader;
    private final Collection<? extends CustomVerify<?>> verifiers;
    private final List<Function<?, ?>> mappers;

    @SuppressWarnings("unchecked")
    protected AbstractCustomPatternElement(AbstractCustomPatternElementBuilder builder) {
        this.id = builder.id;
        this.optional = builder.optional;
        this.reader = builder.reader;
        this.verifiers = builder.verifies;
        this.mappers = builder.mappers;
    }

    @Override
    public List<Function<?, ?>> getMappers() {
        return mappers;
    }

    @Override
    public Collection<? extends CustomVerify<?>> getVerifiers() {
        return verifiers;
    }

    @Override
    public CustomReader<?> getReader() {
        return reader;
    }

    @Override
    public boolean isOptional() {
        return optional;
    }

    @Override
    public String getId() {
        return id;
    }

    public abstract static class AbstractCustomPatternElementBuilder<T, B extends Buildable<T>> implements Buildable<T> {

        protected final String id;
        protected boolean optional;
        protected CustomReader<T> reader;
        protected Collection<CustomVerify<T>> verifies = new ArrayList<>(1);
        protected List<Function<?, ?>> mappers = new ArrayList<>(1);

        protected AbstractCustomPatternElementBuilder(String id) {
            this.id = id;
        }

        public B optional(boolean flag) {
            this.optional = flag;
            return returnThis();
        }

        public B custom(CustomReader<T> reader) {
            this.reader = reader;
            return returnThis();
        }

        public B verify(CustomVerify<T> verify) {
            this.verifies.add(verify);
            return returnThis();
        }

        @SuppressWarnings("unchecked")
        public <V> AbstractCustomPatternElementBuilder<V, ? extends Buildable<V>> map(Function<T, V> mapper) {
            mappers.add(mapper);
            return (AbstractCustomPatternElementBuilder<V, ? extends Buildable<V>>) this;
        }

        @SuppressWarnings("unchecked")
        private B returnThis() {
            return (B) this;
        }

    }

}
