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

package org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.callbacks;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

public class ThisExpressionCallback implements ExpressionCallback {

    private final ClassPrototype type;

    public ThisExpressionCallback(ClassPrototype type) {
        this.type = type;
    }

    @Override
    public Value call(Expression expression, ExecutableBranch branch) {
        return branch.getInstance();
    }

    @Override
    public ClassPrototype getReturnType() {
        return type;
    }

    public static Expression asExpression(ClassPrototype type) {
        return new PandaExpression(new ThisExpressionCallback(type));
    }

    public static Expression of(ParserData data) {
        return asExpression(data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE));
    }

}
