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

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.FieldVisibility;

public class PandaPrototypeFieldBuilder {

    protected ClassPrototype type;
    protected int fieldIndex;
    protected String name;
    protected FieldVisibility visibility;
    protected boolean isStatic;
    protected boolean isNative;
    protected boolean mutable;
    protected boolean nullable;

    public PandaPrototypeFieldBuilder type(ClassPrototype type) {
        this.type = type;
        return this;
    }

    public PandaPrototypeFieldBuilder fieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
        return this;
    }

    public PandaPrototypeFieldBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PandaPrototypeFieldBuilder visibility(FieldVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public PandaPrototypeFieldBuilder mutable(boolean mutable) {
        this.mutable = mutable;
        return this;
    }

    public PandaPrototypeFieldBuilder nullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public PandaPrototypeField build() {
        return new PandaPrototypeField(this);
    }

    public PandaPrototypeFieldBuilder isStatic(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }

    public PandaPrototypeFieldBuilder isNative(boolean isNative) {
        this.isNative = isNative;
        return this;
    }

}
