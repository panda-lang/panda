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

package org.panda_lang.panda.language.structure.prototype.structure.constructor;

import org.panda_lang.panda.core.structure.dynamic.Executable;
import org.panda_lang.panda.core.structure.dynamic.ScopeInstance;
import org.panda_lang.panda.core.structure.util.StatementCell;
import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.core.structure.wrapper.Scope;
import org.panda_lang.panda.language.runtime.ExecutableBridge;

public class ConstructorScopeInstance implements ScopeInstance {

    private final ConstructorScope scope;
    private final Value[] variables;

    public ConstructorScopeInstance(ConstructorScope scope) {
        this.scope = scope;
        this.variables = new Value[scope.getVariables().size()];
    }

    @Override
    public void execute(ExecutableBridge bridge) {
        for (StatementCell statementCell : scope.getStatementCells()) {
            if (!statementCell.isExecutable()) {
                continue;
            }

            Executable executable = (Executable) statementCell.getStatement();
            bridge.call(executable);
        }
    }

    @Override
    public Value[] getVariables() {
        return variables;
    }

    @Override
    public Scope getScope() {
        return scope;
    }

}
