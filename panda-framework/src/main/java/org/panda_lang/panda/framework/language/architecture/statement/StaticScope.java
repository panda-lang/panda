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

package org.panda_lang.panda.framework.language.architecture.statement;

import org.panda_lang.panda.framework.design.architecture.dynamic.ScopeFrame;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.language.architecture.dynamic.AbstractScopeFrame;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;

import java.util.Map;

public final class StaticScope extends AbstractScope {

    private final Map<Variable, Object> variables;

    public StaticScope(Map<Variable, Object> variables) {
        this.variables = variables;
        variables.forEach((variable, value) -> super.addVariable(variable));
    }

    @Override
    public final ScopeFrame createInstance(ExecutableBranch branch) {
        ScopeFrame frame = new StaticFrame(this);

        for (int pointer = 0; pointer < getVariables().size(); pointer++) {
            Variable variable = getVariables().get(pointer);

            if (!variables.containsKey(variable)) {
                continue;
            }

            frame.set(pointer, new PandaValue(variable.getType(), variables.get(variable)));
        }

        return frame;
    }

    private static final class StaticFrame extends AbstractScopeFrame<StaticScope> {

        protected StaticFrame(StaticScope scope) {
            super(scope);
        }

        @Override
        public void execute(ExecutableBranch branch) {
            branch.call(this);
        }

    }

}
