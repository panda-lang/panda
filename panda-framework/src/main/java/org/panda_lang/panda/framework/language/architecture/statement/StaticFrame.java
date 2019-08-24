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

package org.panda_lang.panda.framework.language.architecture.statement;

import org.panda_lang.panda.framework.design.architecture.dynamic.LivingFrame;
import org.panda_lang.panda.framework.design.architecture.statement.VariableData;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.language.architecture.dynamic.AbstractLivingFrame;

import java.util.Map;

public final class StaticFrame extends AbstractFrame {

    private final Map<VariableData, Object> variables;

    public StaticFrame(Map<VariableData, Object> variables) {
        super(null);
        this.variables = variables;
        variables.forEach((data, value) -> createVariable(data));
    }

    @Override
    public final LivingFrame revive(Flow parentFlow) {
        LivingFrame frame = new StaticLivingFrame(this);

        for (Map.Entry<VariableData, Object> entry : variables.entrySet()) {
            //noinspection OptionalGetWithoutIsPresent
            frame.set(getVariable(entry.getKey().getName()).get().getPointer(), entry.getValue());
        }

        return frame;
    }

    private static final class StaticLivingFrame extends AbstractLivingFrame<StaticFrame> {

        protected StaticLivingFrame(StaticFrame scope) {
            super(scope);
        }

        @Override
        public void execute(Flow flow) {
            flow.call(this);
        }

    }

}
