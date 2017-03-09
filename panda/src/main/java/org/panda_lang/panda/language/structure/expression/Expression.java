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

import org.panda_lang.panda.implementation.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBridge;
import org.panda_lang.panda.implementation.structure.dynamic.Executable;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;

public class Expression implements Executable {

    private final ExpressionType type;
    private final ClassPrototype returnType;

    public Expression(ExpressionType type, Value value) {
        this.type = type;
        this.returnType =  value.getType();
    }

    @Override
    public void execute(ExecutableBridge executionInfo) {

    }

    public ExpressionType getType() {
        return type;
    }

}
