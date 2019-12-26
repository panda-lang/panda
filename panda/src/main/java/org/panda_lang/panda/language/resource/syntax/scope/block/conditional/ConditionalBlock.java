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

package org.panda_lang.panda.language.resource.syntax.scope.block.conditional;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.dynamic.ControlledScope;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.architecture.statement.Statement;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.design.runtime.Result;
import org.panda_lang.framework.language.architecture.statement.AbstractBlock;
import org.panda_lang.framework.language.interpreter.parser.PandaParserException;

final class ConditionalBlock extends AbstractBlock implements ControlledScope {

    private final Expression condition;
    private Scope elseBlock;

    public ConditionalBlock(Scope parent, SourceLocation location, Expression condition) {
        super(parent, location);

        if (!Boolean.class.isAssignableFrom(condition.getType().getAssociatedClass().getImplementation())) {
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
            return stack.callStatement(instance, (Statement) elseBlock);
        }

        return null;
    }

    @Override
    public boolean hasEffective(Class<? extends Statement> statementClass) {
        boolean current = super.hasEffective(statementClass);
        boolean otherwise = elseBlock == null || elseBlock.hasEffective(statementClass);

        return current && otherwise;
    }

    public void setElseBlock(Scope elseBlock) {
        this.elseBlock = elseBlock;
    }

}
