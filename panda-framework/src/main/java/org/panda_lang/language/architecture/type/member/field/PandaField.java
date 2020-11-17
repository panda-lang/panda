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

package org.panda_lang.language.architecture.type.member.field;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.expression.ExpressionUtils;
import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.architecture.type.member.AbstractMember;
import org.panda_lang.utilities.commons.function.Lazy;

import java.util.function.Supplier;

public final class PandaField extends AbstractMember<TypeField> implements TypeField {

    private final int fieldIndex;
    private final boolean isStatic;
    private final boolean isNative;
    private final boolean mutable;
    private final boolean nillable;

    private Expression defaultValue;
    private Lazy<?> staticValue;
    private boolean initialized;

    protected PandaField(PandaFieldBuilder builder) {
        super(builder);

        this.fieldIndex = builder.fieldIndex;
        this.isStatic = builder.isStatic;
        this.isNative = builder.isNative;
        this.mutable = builder.mutable;
        this.nillable = builder.nillable;
    }

    @Override
    public synchronized PandaField initialize() {
        this.initialized = true;

        if (isStatic && defaultValue != null) {
            this.staticValue = new Lazy<>(() -> ExpressionUtils.evaluateConstExpression(defaultValue));
        }

        return this;
    }

    @Override
    public void setDefaultValue(Expression defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public void setStaticValue(Supplier<?> staticValue) {
        this.staticValue = new Lazy<>(staticValue);
    }

    @Override
    public boolean isNillable() {
        return nillable;
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public boolean isNative() {
        return isNative;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean hasDefaultValue() {
        return defaultValue != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object fetchStaticValue() {
        return staticValue.get();
    }

    @Override
    public Expression getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Signature getSignature() {
        return getReturnType();
    }

    @Override
    public int getPointer() {
        return fieldIndex;
    }

    @Override
    public String getName() {
        return getSimpleName();
    }

    @Override
    public String toString() {
        return getType().getSimpleName() + "." + this.getName();
    }

    public static PandaFieldBuilder builder() {
        return new PandaFieldBuilder();
    }

    public static final class PandaFieldBuilder extends AbstractMember.PandaParametrizedExecutableBuilder<TypeField, PandaFieldBuilder> {

        protected int fieldIndex;
        protected boolean isStatic;
        protected boolean isNative;
        protected boolean mutable;
        protected boolean nillable;

        private PandaFieldBuilder() { }

        public PandaFieldBuilder fieldIndex(int fieldIndex) {
            this.fieldIndex = fieldIndex;
            return this;
        }

        public PandaFieldBuilder mutable(boolean mutable) {
            this.mutable = mutable;
            return this;
        }

        public PandaFieldBuilder nillable(boolean nillable) {
            this.nillable = nillable;
            return this;
        }

        public PandaFieldBuilder isStatic(boolean isStatic) {
            this.isStatic = isStatic;
            return this;
        }

        public PandaFieldBuilder isNative(boolean isNative) {
            this.isNative = isNative;
            return this;
        }

        public PandaField build() {
            return new PandaField(this);
        }

    }

}
