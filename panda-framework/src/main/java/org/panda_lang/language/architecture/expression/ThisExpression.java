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

package org.panda_lang.language.architecture.expression;

import org.panda_lang.language.architecture.type.PropertyFrame;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.language.architecture.type.TypeComponents;
import org.panda_lang.utilities.commons.ObjectUtils;

public final class ThisExpression implements DynamicExpression {

    private final Type type;

    private ThisExpression(Type type) {
        this.type = type;
    }

    @Override
    public <T> T evaluate(ProcessStack stack, Object instance) {
        return ObjectUtils.cast(instance instanceof PropertyFrame ? ((PropertyFrame) instance).getInstance() : instance);
    }

    @Override
    public Type getReturnType() {
        return type;
    }

    public static Expression of(Type type) {
        return new PandaExpression(new ThisExpression(type));
    }

    public static Expression of(Context context) {
        return of(context.getComponent(TypeComponents.PROTOTYPE));
    }

}
