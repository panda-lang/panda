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

package org.panda_lang.panda.language.structure.expression.callbacks;

import org.panda_lang.panda.core.structure.value.PandaValue;
import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBranch;
import org.panda_lang.panda.language.structure.expression.Expression;
import org.panda_lang.panda.language.structure.expression.ExpressionCallback;
import org.panda_lang.panda.language.structure.expression.ExpressionUtils;
import org.panda_lang.panda.language.structure.prototype.structure.ClassInstance;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.constructor.Constructor;

public class InstanceExpressionCallback implements ExpressionCallback {

    private final ClassPrototype returnType;
    private final Constructor constructor;
    private final Expression[] arguments;

    public InstanceExpressionCallback(ClassPrototype returnType, Constructor constructor, Expression... arguments) {
        this.returnType = returnType;
        this.constructor = constructor;
        this.arguments = arguments;
    }

    @Override
    public Value call(Expression expression, ExecutableBranch bridge) {
        Value[] values = ExpressionUtils.getValues(bridge, arguments);
        ClassInstance instance = constructor.createInstance(bridge, values);

        return new PandaValue(returnType, instance);
    }

    public ClassPrototype getReturnType() {
        return returnType;
    }

}
