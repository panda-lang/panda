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

package org.panda_lang.panda.language.structure.main;

import org.panda_lang.panda.core.structure.dynamic.ScopeInstance;
import org.panda_lang.panda.core.structure.util.ExecutableBridgeUtils;
import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.core.structure.wrapper.Scope;
import org.panda_lang.panda.language.runtime.ExecutableBridge;

public class MainInstance implements ScopeInstance {

    private final Main main;
    private final Value[] variables;

    public MainInstance(Main main) {
        this.main = main;
        this.variables = new Value[main.getVariables().size()];
    }

    @Override
    public void execute(ExecutableBridge bridge) {
        ExecutableBridgeUtils.execute(bridge, main.getStatementCells());
    }

    @Override
    public Value[] getVariables() {
        return variables;
    }

    @Override
    public Scope getScope() {
        return main;
    }

}
