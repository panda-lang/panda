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

package org.panda_lang.framework.language.architecture.type;

import io.vavr.control.Option;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.type.TypeConstructor;
import org.panda_lang.framework.language.architecture.type.utils.ParameterUtils;
import org.panda_lang.utilities.commons.function.CachedSupplier;

import java.util.function.Supplier;

public final class PandaConstructor extends AbstractExecutableProperty<TypeConstructor> implements TypeConstructor {

    private final @Nullable CachedSupplier<Option<BaseCall>> baseCallArgumentsSupplier;

    private PandaConstructor(PandaConstructorBuilder builder) {
        super(builder);
        this.baseCallArgumentsSupplier = builder.baseCallArgumentsSupplier;
    }

    @Override
    public Option<BaseCall> getBaseCall() {
        return Option.of(baseCallArgumentsSupplier).flatMap(Supplier::get);
    }

    @Override
    public String toString() {
        return getType().getName() + "(" + ParameterUtils.toString(getParameters()) + ")";
    }

    public static PandaConstructorBuilder builder() {
        return new PandaConstructorBuilder().name("constructor");
    }

    public static final class PandaConstructorBuilder extends PandaParametrizedExecutableBuilder<TypeConstructor, PandaConstructorBuilder> {

        private CachedSupplier<Option<BaseCall>> baseCallArgumentsSupplier;

        private PandaConstructorBuilder() { }

        public PandaConstructorBuilder baseCall(Supplier<Option<BaseCall>> baseCallArgumentsSupplier) {
            this.baseCallArgumentsSupplier = new CachedSupplier<>(baseCallArgumentsSupplier);
            return this;
        }

        public PandaConstructor build() {
            return new PandaConstructor(this);
        }

    }

}
