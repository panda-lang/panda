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

package org.panda_lang.panda.framework.language.architecture.value;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.value.Variable;

public class PandaVariable implements Variable {

    protected final ClassPrototypeReference type;
    protected final String name;
    protected final boolean mutable;
    protected final boolean nullable;

    public PandaVariable(ClassPrototypeReference type, String name, boolean mutable, boolean nullable) {
        if (type == null) {
            throw new IllegalArgumentException("Variable type cannot be null");
        }

        if (name == null) {
            throw new IllegalArgumentException("Variable name cannot be null");
        }

        this.type = type;
        this.name = name;
        this.mutable = mutable;
        this.nullable = nullable;
    }

    public PandaVariable(ClassPrototypeReference type, String name) {
        this(type, name, false, false);
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    @Override
    public ClassPrototypeReference getTypeReference() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "'" + getName() + "': '" + getType().getName() + "'";
    }

}