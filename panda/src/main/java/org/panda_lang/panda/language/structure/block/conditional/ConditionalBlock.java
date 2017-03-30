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

package org.panda_lang.panda.language.structure.block.conditional;

import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.implementation.structure.util.ExecutableBridgeUtils;
import org.panda_lang.panda.implementation.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBridge;
import org.panda_lang.panda.language.structure.block.PandaBlock;
import org.panda_lang.panda.language.structure.expression.Expression;

public class ConditionalBlock extends PandaBlock {

    private final Expression condition;
    private ConditionalBlock elseBlock;

    public ConditionalBlock(Expression condition) {
        if (!condition.getReturnType().getClassName().equals("Boolean")) {
            throw new PandaParserException("Condition has to return boolean");
        }

        this.condition = condition;
    }

    @Override
    public void execute(ExecutableBridge bridge) {
        condition.execute(bridge);

        Value value = condition.getValue();
        boolean flag = (boolean) value.getValue();

        if (flag) {
            ExecutableBridgeUtils.execute(bridge, getStatementCells());
            return;
        }

        if (elseBlock != null) {
            bridge.call(elseBlock);
        }
    }

    public void setElseBlock(ConditionalBlock elseBlock) {
        this.elseBlock = elseBlock;
    }

}
