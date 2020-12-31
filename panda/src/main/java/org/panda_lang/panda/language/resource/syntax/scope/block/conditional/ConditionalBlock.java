/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.scope.block.conditional;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.dynamic.ControlledScope;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.statement.AbstractBlock;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.architecture.statement.Statement;
import org.panda_lang.language.interpreter.parser.PandaParserException;
import org.panda_lang.language.interpreter.source.Localizable;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.language.runtime.Result;

final class ConditionalBlock extends AbstractBlock implements ControlledScope {

    private final Expression condition;
    private ConditionalBlock elseBlock;

    public ConditionalBlock(Scope parent, Localizable localizable, Expression condition) {
        super(parent, localizable);

        if (!condition.getKnownType().is("panda::Bool")) {
            throw new PandaParserException("Condition has to return boolean");
        }

        this.condition = condition;
    }

    @Override
    public @Nullable Result<?> controlledCall(ProcessStack stack, Object instance) throws Exception {
        Boolean flag = condition.evaluate(stack, instance);

        if (flag) {
            return stack.callScope(instance, this);
        }

        if (elseBlock != null) {
            return stack.callStatement(instance, elseBlock);
        }

        return null;
    }

    @Override
    public boolean hasEffective(Class<? extends Statement> statementClass) {
        boolean current = super.hasEffective(statementClass);
        boolean otherwise = elseBlock == null || elseBlock.hasEffective(statementClass);

        return current && otherwise;
    }

    public void setElseBlock(ConditionalBlock elseBlock) {
        if (this.elseBlock != null) {
            throw new PandaParserException("Else block already set");
        }

        this.elseBlock = elseBlock;
    }

    public ConditionalBlock getElseBlock() {
        return elseBlock;
    }

}
