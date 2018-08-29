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

package org.panda_lang.panda.framework.language.architecture.dynamic.block.looping;

import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlow;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlowCaller;
import org.panda_lang.panda.framework.language.architecture.dynamic.AbstractBlock;

public class LoopBlock extends AbstractBlock implements ControlFlowCaller {

    private final Expression expression;

    public LoopBlock(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(ExecutableBranch branch) {
        branch.callFlow(getStatementCells(), this);
    }

    @Override
    public void call(ExecutableBranch branch, ControlFlow flow) {
        Value value = expression.getExpressionValue(branch);
        int times = value.getValue();

        for (int i = 0; i < times; i++) {
            flow.reset();
            flow.call();

            if (flow.isEscaped() || branch.isInterrupted()) {
                break;
            }
        }
    }

}
