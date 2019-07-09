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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

public class PandaDynamicValue implements Value {

    private final Expression expression;
    private final Flow copyOfBranch;

    private PandaDynamicValue(Expression expression, Flow copyOfBranch) {
        this.expression = expression;
        this.copyOfBranch = copyOfBranch;
    }

    @Override
    public Object getObject() {
        return expression.evaluate(copyOfBranch).getObject();
    }

    @Override
    public ClassPrototype getType() {
        return expression.getReturnType();
    }

    public static Value of(Expression expression, @Nullable Flow flow) {
        return new PandaDynamicValue(expression, flow != null ? flow.duplicate() : null);
    }

}
