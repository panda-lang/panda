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

package org.panda_lang.panda.language.structure.prototype.structure.method.variant;

import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBranch;
import org.panda_lang.panda.language.structure.prototype.scope.ClassScopeInstance;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodCallback;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodScope;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodScopeInstance;
import org.panda_lang.panda.language.structure.prototype.structure.parameter.ParameterUtils;

public class PandaMethodCallback implements MethodCallback<ClassScopeInstance> {

    private final MethodScope scope;

    public PandaMethodCallback(MethodScope scope) {
        this.scope = scope;
    }

    @Override
    public void invoke(ExecutableBranch bridge, ClassScopeInstance instance, Value... parameters) {
        bridge.instance(instance);

        MethodScopeInstance scopeInstance = scope.createInstance();
        ParameterUtils.assignValues(scopeInstance.getVariables(), parameters);

        bridge.call(scopeInstance);
    }

}
