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

package org.panda_lang.panda.language.architecture.prototype.standard.method;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.LivingFrame;
import org.panda_lang.panda.framework.design.runtime.ProcessStack;
import org.panda_lang.panda.framework.design.runtime.Result;
import org.panda_lang.panda.language.architecture.prototype.standard.parameter.ParameterUtils;
import org.panda_lang.panda.language.architecture.prototype.standard.parameter.ParametrizedExecutableCallback;

public class PandaMethodCallback implements ParametrizedExecutableCallback<LivingFrame> {

    private final MethodFrame scope;

    public PandaMethodCallback(MethodFrame scope) {
        this.scope = scope;
    }

    @Override
    public @Nullable Object invoke(ProcessStack stack, @Nullable LivingFrame instance, Object[] arguments) {
        MethodFrame.MethodLivingFrame scopeInstance = scope.revive(stack, instance);
        ParameterUtils.assignValues(scopeInstance, arguments);
        Result<?> result = stack.call(scopeInstance, scopeInstance);

        if (result == null) {
            return null;
        }

        return result.getResult();
    }

}
