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

package org.panda_lang.language.architecture.type.member.constructor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.dynamic.AbstractExecutableStatement;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.expression.ExpressionEvaluator;
import org.panda_lang.language.architecture.expression.ExpressionUtils;
import org.panda_lang.language.architecture.type.TypeInstance;
import org.panda_lang.language.architecture.type.member.constructor.ConstructorScope.ConstructorFrame;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.utilities.commons.function.Option;

import java.util.List;

public final class BaseCall extends AbstractExecutableStatement implements ExpressionEvaluator {

    private final TypeConstructor baseConstructor;
    private final List<Expression> arguments;

    public BaseCall(Location location, TypeConstructor baseConstructor, List<Expression> arguments) {
        super(location);
        this.baseConstructor = baseConstructor;
        this.arguments = arguments;
    }

    @Override
    public @Nullable Object execute(ProcessStack stack, Object instance) throws Exception {
        Option<ConstructorScope> constructorScope = baseConstructor.getConstructorScope();

        if (constructorScope.isEmpty()) {
            return instance;
        }

        ConstructorFrame constructorFrame = (ConstructorFrame) instance;
        TypeInstance typeInstance = constructorFrame.getTypeFrame().getTypeInstance();

        return constructorScope.get().invoke(baseConstructor, stack, typeInstance, typeInstance.__panda__get_frame().getBaseArguments());
    }

    @Override
    public Object[] evaluate(ProcessStack stack, @Nullable Object instance) throws Exception {
        return ExpressionUtils.evaluate(stack, instance, arguments);
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public TypeConstructor getBaseConstructor() {
        return baseConstructor;
    }

}
