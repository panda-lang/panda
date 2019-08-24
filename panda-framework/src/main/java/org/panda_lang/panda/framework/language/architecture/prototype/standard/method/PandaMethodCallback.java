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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter.ParameterUtils;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter.ParametrizedExecutableCallback;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.structure.ClassPrototypeLivingFrame;
import org.panda_lang.panda.framework.language.runtime.PandaFlow;

public class PandaMethodCallback implements ParametrizedExecutableCallback<ClassPrototypeLivingFrame> {

    private final MethodFrame scope;

    public PandaMethodCallback(MethodFrame scope) {
        this.scope = scope;
    }

    @Override
    public Object invoke(Flow flow, @Nullable ClassPrototypeLivingFrame instance, Object[] arguments) {
        Flow subFlow = new PandaFlow(flow, instance);

        MethodLivingFrame scopeInstance = scope.revive(subFlow);
        ParameterUtils.assignValues(scopeInstance, arguments);

        Flow methodBranch = subFlow.call(scopeInstance);
        subFlow.setReturnValue(methodBranch.getReturnedValue());

        return methodBranch.getReturnedValue();
    }

}
