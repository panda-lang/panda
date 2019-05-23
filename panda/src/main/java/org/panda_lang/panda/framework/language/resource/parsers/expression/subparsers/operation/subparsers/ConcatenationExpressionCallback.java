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

package org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.operation.subparsers;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.resource.PandaTypes;

import java.util.List;

public class ConcatenationExpressionCallback implements ExpressionCallback {

    private final List<Expression> values;

    public ConcatenationExpressionCallback(List<Expression> values) {
        this.values = values;
    }

    @Override
    public Value call(Expression expression, ExecutableBranch branch) {
        StringBuilder content = new StringBuilder();

        for (Expression value : values) {
            content.append(value.getExpressionValue(branch).getObject());
        }

        return new PandaValue(PandaTypes.STRING, content);
    }

    @Override
    public ClassPrototype getReturnType() {
        return PandaTypes.STRING;
    }

}
