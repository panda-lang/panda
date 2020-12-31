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

package org.panda_lang.language.architecture.type.signature;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.expression.ExpressionUtils;
import org.panda_lang.language.architecture.expression.ExpressionValueType;
import org.panda_lang.language.architecture.type.member.MemberInvoker;
import org.panda_lang.language.architecture.type.member.ParametrizedMember;
import org.panda_lang.language.runtime.ProcessStack;

import java.util.List;

public final class AdjustedExpression implements Expression {

    private final ParametrizedMember member;
    private final MemberInvoker<ParametrizedMember, Object, Object> instanceInvoker;
    private final List<? extends Expression> arguments;
    private final Signature returnType;

    public AdjustedExpression(MemberInvoker<ParametrizedMember, Object, Object> instanceInvoker, Signature instanceSignature, ParametrizedMember member, List<? extends Expression> arguments) {
        this.member = member;
        this.instanceInvoker = instanceInvoker;
        this.arguments = arguments;

        if (instanceSignature == null) {
            this.returnType = member.getReturnType();
        }
        else {
            this.returnType = member.getReturnType().apply(instanceSignature);
        }
    }

    public AdjustedExpression(Expression instance, ParametrizedMember member, List<? extends Expression> arguments) {
        this((property, stack, currentInstance, args) -> instance.evaluate(stack, currentInstance), instance.getSignature(), member, arguments);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) throws Exception {
        Object[] values = ExpressionUtils.evaluate(stack, instance, arguments);

        if (instanceInvoker != null) {
            instance = instanceInvoker.invoke(member, stack, instance, values);
        }

        return member.invoke(stack, instance, values);
    }

    @Override
    public Signature getSignature() {
        return returnType;
    }

    @Override
    public ExpressionValueType getExpressionType() {
        return ExpressionValueType.DYNAMIC;
    }

    @Override
    public String toString() {
        return member + " -> " + getSignature();
    }

}
