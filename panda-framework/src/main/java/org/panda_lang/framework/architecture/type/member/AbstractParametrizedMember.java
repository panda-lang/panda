/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.architecture.type.member;

import org.panda_lang.framework.architecture.type.Type;
import org.panda_lang.framework.architecture.type.Typed;
import org.panda_lang.framework.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.framework.architecture.type.signature.Signature;
import org.panda_lang.framework.runtime.ProcessStack;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractParametrizedMember<E extends Member> extends AbstractMember<E> implements ParametrizedMember {

    private final List<? extends PropertyParameter> parameters;
    private final MemberInvoker<E, Object, Object> invoker;

    protected AbstractParametrizedMember(PandaParametrizedExecutableBuilder<E, ?> builder) {
        super(builder);

        this.parameters = builder.parameters;
        this.invoker = builder.invoker;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(ProcessStack stack, Object instance, Object... arguments) throws Exception {
        return invoker.invoke((E) this, stack, instance, arguments);
    }

    @Override
    public boolean isInvokableBy(List<? extends Typed> arguments) {
        if (getParameters().size() != arguments.size()) {
            return false;
        }

        for (int index = 0; index < parameters.size(); index++) {
            PropertyParameter parameter = parameters.get(index);
            Type type = arguments.get(index).toType();

            if (!parameter.getKnownType().isAssignableFrom(type)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<Signature> getParameterSignatures() {
        return getParameters().stream()
                .map(PropertyParameter::getSignature)
                .collect(Collectors.toList());
    }

    @Override
    public List<? extends PropertyParameter> getParameters() {
        return parameters;
    }

}
