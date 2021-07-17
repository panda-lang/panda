/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.architecture.type.member.constructor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.architecture.type.member.AbstractParametrizedMember;
import org.panda_lang.framework.architecture.type.member.parameter.ParameterUtils;
import panda.std.Lazy;
import panda.std.Option;

import java.util.function.Supplier;

public final class PandaConstructor extends AbstractParametrizedMember<TypeConstructor> implements TypeConstructor {

    private final @Nullable ConstructorScope constructorScope;
    private final @Nullable Lazy<Option<BaseCall>> baseCallArgumentsSupplier;

    private PandaConstructor(PandaConstructorBuilder builder) {
        super(builder);
        this.constructorScope = builder.constructorScope;
        this.baseCallArgumentsSupplier = builder.baseCallSupplier;
    }

    @Override
    public Option<BaseCall> getBaseCall() {
        return Option.of(baseCallArgumentsSupplier).flatMap(Supplier::get);
    }

    @Override
    public Option<ConstructorScope> getConstructorScope() {
        return Option.of(constructorScope);
    }

    @Override
    public String toString() {
        return getType().getName() + "(" + ParameterUtils.toString(getParameters()) + ")";
    }

    public static PandaConstructorBuilder builder() {
        return new PandaConstructorBuilder().name("constructor");
    }

    public static final class PandaConstructorBuilder extends PandaParametrizedExecutableBuilder<TypeConstructor, PandaConstructorBuilder> {

        private ConstructorScope constructorScope;
        private Lazy<Option<BaseCall>> baseCallSupplier;

        private PandaConstructorBuilder() { }

        public PandaConstructorBuilder scope(ConstructorScope scope) {
            this.constructorScope = scope;
            return this;
        }

        public PandaConstructorBuilder baseCall(Supplier<Option<BaseCall>> baseCallSupplier) {
            this.baseCallSupplier = new Lazy<>(baseCallSupplier);
            return this;
        }

        public PandaConstructor build() {
            return new PandaConstructor(this);
        }

    }

}
