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

package panda.interpreter.syntax.type;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.dynamic.AbstractExecutableStatement;
import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.expression.ExpressionUtils;
import panda.interpreter.architecture.type.member.constructor.ConstructorScope.ConstructorFrame;
import panda.interpreter.architecture.type.member.constructor.TypeConstructor;
import panda.interpreter.architecture.type.signature.AdjustedMember;
import panda.interpreter.source.Localizable;
import panda.interpreter.runtime.ProcessStack;

import java.util.List;

public final class SelfConstructor extends AbstractExecutableStatement {

    private final TypeConstructor constructor;
    private final List<Expression> arguments;

    public SelfConstructor(Localizable localizable, TypeConstructor constructor, List<Expression> arguments) {
        super(localizable);
        this.constructor = constructor;
        this.arguments = arguments;
    }

    public SelfConstructor(Localizable localizable, AdjustedMember<TypeConstructor> adjustedMember) {
        this(localizable, adjustedMember.getExecutable(), adjustedMember.getArguments());
    }

    @Override
    public @Nullable Object execute(ProcessStack stack, Object instance) throws Exception {
        ConstructorFrame constructorFrame = (ConstructorFrame) instance;
        return constructor.invoke(stack, constructorFrame.getInstance(), ExpressionUtils.evaluate(stack, instance, arguments));
    }

}
