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

package org.panda_lang.framework.language.architecture.dynamic.accessor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.Typed;
import org.panda_lang.framework.design.architecture.statement.Variable;
import org.panda_lang.framework.design.interpreter.source.Location;
import org.panda_lang.framework.design.runtime.MemoryContainer;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.dynamic.assigner.Assigner;

public interface Accessor<T extends Variable> extends Typed {

    MemoryContainer fetchMemoryContainer(ProcessStack stack, Object instance) throws Exception;

    Assigner<T> toAssigner(Location location, boolean initialize, Expression value);

    @Nullable <R> R getValue(ProcessStack stack, Object instance) throws Exception;

    @Override
    default Type getType() {
        return getVariable().getType();
    }

    int getMemoryPointer();

    T getVariable();

}
