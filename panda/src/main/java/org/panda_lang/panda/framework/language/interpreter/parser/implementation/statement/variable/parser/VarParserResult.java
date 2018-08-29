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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable.parser;

import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

public class VarParserResult {

    private final Variable variable;
    private final Expression instanceExpression;
    private final Scope scope;
    private final boolean freshVariable;
    private final boolean local;

    public VarParserResult(Expression instanceExpression, Variable variable, boolean freshVariable, Scope scope, boolean local) {
        this.instanceExpression = instanceExpression;
        this.variable = variable;
        this.freshVariable = freshVariable;
        this.scope = scope;
        this.local = local;
    }

    public boolean isLocal() {
        return local;
    }

    public boolean isFreshVariable() {
        return freshVariable;
    }

    public Scope getScope() {
        return scope;
    }

    public Expression getInstanceExpression() {
        return instanceExpression;
    }

    public Variable getVariable() {
        return variable;
    }

}
