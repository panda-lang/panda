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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.StaticValue;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

public class PandaStaticValue implements StaticValue {

    private final boolean external;
    private final Value value;
    private final Expression expression;
    private final ExecutableBranch copyOfBranch;

    private PandaStaticValue(Value value) {
        this(false, value, null, null);
    }

    private PandaStaticValue(Expression expression, ExecutableBranch copyOfBranch) {
        this(true, null, expression, copyOfBranch);
    }

    private PandaStaticValue(boolean external, Value value, Expression expression, ExecutableBranch copyOfBranch) {
        this.external = external;
        this.value = value;
        this.expression = expression;
        this.copyOfBranch = copyOfBranch;
    }

    @Override
    public Value getValue() {
        return external ? expression.getExpressionValue(copyOfBranch) : value;
    }

    @Override
    public ClassPrototype getReturnType() {
        return external ? expression.getReturnType() : value.getType();
    }

    public static StaticValue of(Expression expression, @Nullable ExecutableBranch branch) {
        return new PandaStaticValue(expression, branch != null ? branch.duplicate() : null);
    }

    public static StaticValue of(Value value) {
        return new PandaStaticValue(value);
    }

}
