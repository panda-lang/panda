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

package org.panda_lang.panda.language.interpreter.parser.scope.block.conditional;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.dynamic.ControlledScope;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.design.runtime.Result;
import org.panda_lang.framework.language.architecture.statement.AbstractBlock;
import org.panda_lang.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.framework.language.resource.internal.java.JavaModule;

class ConditionalBlock extends AbstractBlock implements ControlledScope {

    private final Expression condition;
    private Scope elseBlock;

    public ConditionalBlock(Scope parent, SourceLocation location, Expression condition) {
        super(parent, location);

        if (!JavaModule.BOOLEAN.isAssignableFrom(condition.getReturnType())) {
            throw new PandaParserException("Condition has to return boolean");
        }

        this.condition = condition;
    }

    @Override
    public @Nullable Result<?> controlledCall(ProcessStack stack, Object instance) throws Exception {
        Boolean flag = condition.evaluate(stack, instance);

        if (flag) {
            return stack.call(instance, this);
        }

        if (elseBlock != null) {
            return stack.call(instance, elseBlock);
        }

        return null;
    }

    public void setElseBlock(Scope elseBlock) {
        this.elseBlock = elseBlock;
    }

}
