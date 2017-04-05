/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.structure.expression;

import org.panda_lang.panda.core.structure.dynamic.Executable;
import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBranch;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;

public class Expression implements Executable {

    private final ExpressionType type;
    private final ClassPrototype returnType;
    private final ExpressionCallback callback;
    private Value value;

    public Expression(Value value) {
        this.type = ExpressionType.KNOWN;
        this.returnType = value.getType();
        this.callback = null;
        this.value = value;
    }

    public Expression(ClassPrototype returnType, ExpressionCallback callback) {
        this.type = ExpressionType.UNKNOWN;
        this.returnType = returnType;
        this.callback = callback;
    }

    public Expression(Value value, ExpressionCallback callback) {
        this.type = ExpressionType.BOTH;
        this.returnType = value.getType();
        this.callback = callback;
        this.value = value;
    }

    @Override
    public void execute(ExecutableBranch branch) {
        if (type == ExpressionType.UNKNOWN || type == ExpressionType.BOTH) {
            this.value = callback.call(this, branch);
        }
    }

    public Value getExpressionValue() {
        return value;
    }

    public ClassPrototype getReturnType() {
        return returnType;
    }

    public ExpressionType getType() {
        return type;
    }

    @Override
    public String toString() {
        String s = type.name() + ":" + returnType.getClassName();
        return ExpressionType.KNOWN == type ? s + ":" + value.getValue() : s;
    }

}
