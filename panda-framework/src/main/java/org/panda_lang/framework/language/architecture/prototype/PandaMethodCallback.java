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

package org.panda_lang.framework.language.architecture.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.dynamic.Frame;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.design.runtime.Result;

public class PandaMethodCallback implements PrototypeExecutableCallback<Frame> {

    private final MethodScope scope;

    public PandaMethodCallback(MethodScope scope) {
        this.scope = scope;
    }

    @Override
    public @Nullable Object invoke(ProcessStack stack, @Nullable Frame instance, Object[] arguments) throws Exception {
        MethodScope.MethodFrame scopeInstance = scope.revive(stack, instance);
        ParameterUtils.assignValues(scopeInstance, arguments);
        Result<?> result = stack.call(scopeInstance, scopeInstance);

        if (result == null) {
            return null;
        }

        return result.getResult();
    }

}
