/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.type;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.expression.ExpressionUtils;
import org.panda_lang.framework.design.architecture.type.Adjustment;
import org.panda_lang.framework.design.architecture.type.TypeConstructor;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.dynamic.AbstractExecutableStatement;

public final class BaseConstructor extends AbstractExecutableStatement {

    private final TypeConstructor constructor;
    private final Expression[] arguments;

    public BaseConstructor(SourceLocation location, TypeConstructor constructor, Expression[] arguments) {
        super(location);
        this.constructor = constructor;
        this.arguments = arguments;
    }

    public BaseConstructor(SourceLocation location, Adjustment<TypeConstructor> adjustment) {
        this(location, adjustment.getExecutable(), adjustment.getArguments());
    }

    @Override
    public @Nullable Object execute(ProcessStack stack, Object instance) throws Exception {
        return constructor.invoke(stack, instance, ExpressionUtils.evaluate(stack, instance, arguments));
    }

}
