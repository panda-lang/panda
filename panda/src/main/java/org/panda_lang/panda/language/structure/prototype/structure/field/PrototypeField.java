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

package org.panda_lang.panda.language.structure.prototype.structure.field;

import org.panda_lang.panda.core.structure.value.PandaVariable;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;

public class PrototypeField extends PandaVariable {

    private final int fieldIndex;
    private FieldVisibility visibility;
    private boolean isStatic;
    private boolean isNative;
    private Expression defaultValue;
    private StaticValue staticValue;

    public PrototypeField(ClassPrototype type, int fieldIndex, String name, FieldVisibility visibility, boolean isStatic, boolean isNative) {
        super(type, name, 0);

        this.fieldIndex = fieldIndex;
        this.visibility = visibility;
        this.isStatic = isStatic;
    }

    public void setDefaultValue(Expression defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setStaticValue(StaticValue staticValue) {
        this.staticValue = staticValue;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isNative() {
        return isNative;
    }

    public boolean hasDefaultValue() {
        return defaultValue != null;
    }

    public StaticValue getStaticValue() {
        return staticValue;
    }

    public Expression getDefaultValue() {
        return defaultValue;
    }

    public FieldVisibility getVisibility() {
        return visibility;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

}
