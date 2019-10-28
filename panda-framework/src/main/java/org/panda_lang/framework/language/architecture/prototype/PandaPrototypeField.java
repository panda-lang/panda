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

package org.panda_lang.framework.language.architecture.prototype;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;

public class PandaPrototypeField extends AbstractExecutableProperty implements PrototypeField {

    private final int fieldIndex;
    private final boolean isStatic;
    private final boolean isNative;
    private final boolean mutable;
    private final boolean nillable;

    private Expression defaultValue;
    private Object staticValue;
    private boolean initialized;

    protected PandaPrototypeField(PandaPrototypeFieldBuilder builder) {
        super(builder);

        this.fieldIndex = builder.fieldIndex;
        this.isStatic = builder.isStatic;
        this.isNative = builder.isNative;
        this.mutable = builder.mutable;
        this.nillable = builder.nillable;
    }

    @Override
    public void initialize() {
        this.initialized = true;
    }

    @Override
    public void setDefaultValue(Expression defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public void setStaticValue(Object staticValue) {
        this.staticValue = staticValue;
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
    public Object getStaticValue() {
        return staticValue;
    }

    @Override
    public Expression getDefaultValue() {
        return defaultValue;
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
        return "field" + getPropertyName();
    }

    public static PandaPrototypeFieldBuilder builder() {
        return new PandaPrototypeFieldBuilder();
    }

    public static class PandaPrototypeFieldBuilder extends AbstractExecutableProperty.PandaParametrizedExecutableBuilder<PandaPrototypeFieldBuilder> {

        protected int fieldIndex;
        protected boolean isStatic;
        protected boolean isNative;
        protected boolean mutable;
        protected boolean nillable;

        private PandaPrototypeFieldBuilder() { }

        public PandaPrototypeFieldBuilder fieldIndex(int fieldIndex) {
            this.fieldIndex = fieldIndex;
            return this;
        }

        public PandaPrototypeFieldBuilder mutable(boolean mutable) {
            this.mutable = mutable;
            return this;
        }

        public PandaPrototypeFieldBuilder nillable(boolean nillable) {
            this.nillable = nillable;
            return this;
        }

        public PandaPrototypeFieldBuilder isStatic(boolean isStatic) {
            this.isStatic = isStatic;
            return this;
        }

        public PandaPrototypeFieldBuilder isNative(boolean isNative) {
            this.isNative = isNative;
            return this;
        }

        public PandaPrototypeField build() {
            return new PandaPrototypeField(this);
        }

    }

}
