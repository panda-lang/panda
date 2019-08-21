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

package org.panda_lang.panda.framework.language.runtime.expression;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;

public class ThisExpression implements DynamicExpression {

    private final ClassPrototype type;

    private ThisExpression(ClassPrototype type) {
        this.type = type;
    }

    @Override
    public <T> T call(Expression expression, Flow flow) {
        return flow.getInstance();
    }

    @Override
    public ClassPrototype getReturnType() {
        return type;
    }

    public static Expression of(ClassPrototype type) {
        return new PandaExpression(new ThisExpression(type));
    }

    public static Expression of(Context context) {
        return of(context.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE));
    }

}
