/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.architecture.prototype.field;

import org.panda_lang.panda.framework.design.architecture.prototype.field.FieldVisibility;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.value.StaticValue;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaVariable;

public class PandaPrototypeField extends PandaVariable implements PrototypeField {

    private final int fieldIndex;
    private FieldVisibility visibility;
    private boolean isStatic;
    private boolean isNative;
    private Expression defaultValue;
    private StaticValue staticValue;

    protected PandaPrototypeField(PandaPrototypeFieldBuilder builder) {
        super(builder.type, builder.name, 0, builder.mutable, builder.nullable);

        this.fieldIndex = builder.fieldIndex;
        this.visibility = builder.visibility;
        this.isStatic = builder.isStatic;
        this.isNative = builder.isNative;
    }

    @Override
    public void setDefaultValue(Expression defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public void setStaticValue(StaticValue staticValue) {
        this.staticValue = staticValue;
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
    public boolean hasDefaultValue() {
        return defaultValue != null;
    }

    @Override
    public StaticValue getStaticValue() {
        return staticValue;
    }

    @Override
    public Expression getDefaultValue() {
        return defaultValue;
    }

    @Override
    public FieldVisibility getVisibility() {
        return visibility;
    }

    @Override
    public int getFieldIndex() {
        return fieldIndex;
    }

    public static PandaPrototypeFieldBuilder builder() {
        return new PandaPrototypeFieldBuilder();
    }

}
