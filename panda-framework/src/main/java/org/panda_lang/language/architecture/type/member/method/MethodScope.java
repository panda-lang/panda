/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.architecture.type.member.method;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.dynamic.Frame;
import org.panda_lang.language.architecture.dynamic.Frameable;
import org.panda_lang.language.architecture.statement.AbstractPropertyFramedScope;
import org.panda_lang.language.architecture.type.member.MemberFrameImpl;
import org.panda_lang.language.architecture.type.member.MemberInvoker;
import org.panda_lang.language.architecture.type.member.parameter.ParameterUtils;
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.language.interpreter.source.Localizable;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.language.runtime.Result;

import java.util.List;

public final class MethodScope extends AbstractPropertyFramedScope implements MemberInvoker<TypeMethod, Frameable, Object> {

    public MethodScope(Localizable localizable, List<PropertyParameter> parameters) {
        super(localizable, parameters);
    }

    @Override
    public MethodFrame revive(ProcessStack stack, Object instance) {
        return new MethodFrame(this, (Frame) instance);
    }

    @Override
    public @Nullable Object invoke(TypeMethod method, ProcessStack stack, @Nullable Frameable instance, Object[] arguments) throws Exception {
        MethodFrame scopeInstance = revive(stack, instance != null ? instance.__panda__to_frame() : null);
        ParameterUtils.assignValues(scopeInstance, arguments);
        Result<?> result = stack.callFrame(scopeInstance, scopeInstance);

        if (result == null) {
            return null;
        }

        return result.getResult();
    }

    public static final class MethodFrame extends MemberFrameImpl<MethodScope> {

        public MethodFrame(MethodScope method, Frame instance) {
            super(method, instance);
        }

    }

}
