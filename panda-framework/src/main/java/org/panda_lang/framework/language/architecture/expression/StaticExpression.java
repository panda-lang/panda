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

package org.panda_lang.framework.language.architecture.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.expression.ExpressionValueType;
import org.panda_lang.framework.design.runtime.ProcessStack;

public final class StaticExpression implements Expression {

    private final PrototypeReference reference;

    public StaticExpression(PrototypeReference reference) {
        this.reference = reference;
    }

    @Override
    public @Nullable <T> T evaluate(ProcessStack stack, Object instance) {
        return null;
    }

    @Override
    public Prototype getReturnType() {
        return reference.fetch();
    }

    @Override
    public ExpressionValueType getType() {
        return ExpressionValueType.CONST;
    }

    @Override
    public String toString() {
        return "static " + getReturnType().getName();
    }

}
