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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation;

import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.interpreter.parser.ContextComponent;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.architecture.expression.Expression;

public final class AssignationComponents {

    public static final String CONTEXT_LABEL = "context";
    public static final ContextComponent<ExpressionContext> CONTEXT = ContextComponent.of(CONTEXT_LABEL, ExpressionContext.class);

    public static final String SCOPE_LABEL = "assignation-scope";
    public static final ContextComponent<Scope> SCOPE = ContextComponent.of(SCOPE_LABEL, Scope.class);

    public static final String EXPRESSION_LABEL = "assignation-expression";
    public static final ContextComponent<Expression> EXPRESSION = ContextComponent.of(EXPRESSION_LABEL, Expression.class);

    private AssignationComponents() { }

}
