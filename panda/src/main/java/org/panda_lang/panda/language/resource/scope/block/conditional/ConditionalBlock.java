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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.dynamic.Block;
import org.panda_lang.framework.design.architecture.dynamic.ControlledBlock;
import org.panda_lang.framework.design.architecture.dynamic.Scope;
import org.panda_lang.framework.design.runtime.Result;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.dynamic.AbstractBlock;
import org.panda_lang.framework.language.interpreter.parser.PandaParserException;

class ConditionalBlock extends AbstractBlock implements ControlledBlock {

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
    public @Nullable Result<?> controlledCall(ProcessStack stack, Object instance) {
        Boolean flag = condition.evaluate(stack, instance);

        if (flag) {
            return stack.call(instance, this);
        }

        if (elseBlock != null) {
            return stack.call(instance, elseBlock);
        }

        return null;
    }

    public void setElseBlock(Block elseBlock) {
        this.elseBlock = elseBlock;
    }

}
