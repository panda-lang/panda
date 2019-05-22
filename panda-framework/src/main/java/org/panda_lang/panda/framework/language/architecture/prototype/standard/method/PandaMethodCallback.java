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

package org.panda_lang.panda.framework.language.architecture.prototype.standard.method;

import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodCallback;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter.ParameterUtils;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.structure.ClassPrototypeScopeFrame;

public class PandaMethodCallback implements MethodCallback<ClassPrototypeScopeFrame> {

    private final MethodScope scope;

    public PandaMethodCallback(MethodScope scope) {
        this.scope = scope;
    }

    @Override
    public void invoke(ExecutableBranch branch, ClassPrototypeScopeFrame instance, Value... parameters) {
        branch.instance(instance != null ? instance.toValue() : null);

        MethodScopeFrame scopeInstance = scope.createInstance(branch);
        ParameterUtils.assignValues(scopeInstance, parameters);

        ExecutableBranch methodBranch = branch.call(scopeInstance);
        branch.setReturnValue(methodBranch.getReturnedValue());
    }

}
