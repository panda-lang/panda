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

package org.panda_lang.panda.language.interpreter.parser.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionTransactionCommit;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

import java.util.Collection;

final class PandaExpressionTransaction implements ExpressionTransaction {

    private final Collection<ExpressionTransactionCommit> commits;
    private Expression expression;

    PandaExpressionTransaction(@Nullable Expression expression, Collection<ExpressionTransactionCommit> commits) {
        this.expression = expression;
        this.commits = commits;
    }

    @Override
    public void rollback() {
        commits.forEach(ExpressionTransactionCommit::rollback);
    }

    PandaExpressionTransaction withExpression(Expression expression) {
        this.expression = expression;
        return this;
    }

    @Override
    public Collection<ExpressionTransactionCommit> getCommits() {
        return commits;
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

}
