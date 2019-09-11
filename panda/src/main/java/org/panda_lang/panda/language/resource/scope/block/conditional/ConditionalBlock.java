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

package org.panda_lang.panda.language.resource.scope.block.conditional;

import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.language.architecture.dynamic.AbstractBlock;
import org.panda_lang.panda.language.interpreter.parser.PandaParserException;

class ConditionalBlock extends AbstractBlock {

    private final Expression condition;
    private Block elseBlock;

    public ConditionalBlock(Scope parent, Expression condition) {
        super(parent);

        if (!condition.getReturnType().isClassOf("Boolean")) {
            throw new PandaParserException("Condition has to return boolean");
        }

        this.condition = condition;
    }

    @Override
    public void execute(Flow flow) {
        Boolean flag = condition.evaluate(flow);

        if (flag) {
            flow.call(super.getCells());
            return;
        }

        if (elseBlock != null) {
            flow.call(elseBlock);
        }
    }

    public void setElseBlock(Block elseBlock) {
        this.elseBlock = elseBlock;
    }

}
