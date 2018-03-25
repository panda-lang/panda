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

package org.panda_lang.panda.framework.language.architecture.value;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Variable;

public class PandaVariable implements Variable {

    protected final ClassPrototype variableType;
    protected final String variableName;
    protected final int nestingLevel;
    protected final boolean mutable;
    protected final boolean nullable;

    public PandaVariable(ClassPrototype variableType, String variableName, int nestingLevel, boolean mutable, boolean nullable) {
        this.variableType = variableType;
        this.variableName = variableName;
        this.nestingLevel = nestingLevel;
        this.mutable = mutable;
        this.nullable = nullable;
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
    public int getNestingLevel() {
        return nestingLevel;
    }

    @Override
    public ClassPrototype getType() {
        return variableType;
    }

    @Override
    public String getName() {
        return variableName;
    }

    @Override
    public String toString() {
        return "'" + getName() + "': '" + getType().getClassName() + "'";
    }

}