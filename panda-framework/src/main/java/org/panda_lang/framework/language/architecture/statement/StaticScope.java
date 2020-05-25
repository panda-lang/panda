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

package org.panda_lang.framework.language.architecture.statement;

import org.panda_lang.framework.design.architecture.dynamic.Frame;
import org.panda_lang.framework.design.architecture.statement.StandardizedFramedScope;
import org.panda_lang.framework.design.architecture.statement.VariableData;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.dynamic.AbstractFrame;

import java.util.Map;

public final class StaticScope extends AbstractFramedScope implements StandardizedFramedScope {

    private final Map<VariableData, Object> variables;

    public StaticScope(Map<VariableData, Object> variables) {
        super(null);
        this.variables = variables;
        variables.forEach((data, value) -> createVariable(data));
    }

    @Override
    public final Frame revive(ProcessStack parentStack, Object instance) {
        Frame frame = new StaticFrame(this);

        for (Map.Entry<VariableData, Object> entry : variables.entrySet()) {
            //noinspection OptionalGetWithoutIsPresent
            frame.set(getVariable(entry.getKey().getName()).get().getPointer(), entry.getValue());
        }

        return frame;
    }

    private static final class StaticFrame extends AbstractFrame<StaticScope> {

        protected StaticFrame(StaticScope scope) {
            super(scope);
        }

    }

}
