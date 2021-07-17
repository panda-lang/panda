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

package panda.repl;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.dynamic.Frame;
import panda.interpreter.architecture.statement.AbstractPropertyFramedScope;
import panda.interpreter.architecture.statement.Variable;
import panda.interpreter.architecture.type.TypeFrame;
import panda.interpreter.architecture.type.member.parameter.PropertyParameter;
import panda.interpreter.source.Location;
import panda.interpreter.runtime.ProcessStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class ReplScope extends AbstractPropertyFramedScope {

    private final Map<Integer, Object> defaultValues = new HashMap<>();
    private final Collection<ReplVariableChangeListener> variableChangeListeners = new ArrayList<>(1);

    ReplScope(Location location, List<PropertyParameter> parameters) {
        super(location, parameters);
    }

    @Override
    public Frame revive(ProcessStack stack, Object instance) {
        ReplFrame frame = new ReplFrame(this, (TypeFrame) instance);

        for (Entry<Integer, Object> entry : defaultValues.entrySet()) {
            frame.set(entry.getKey(), entry.getValue());
        }

        return frame;
    }

    protected void addVariableChangeListener(ReplVariableChangeListener variableChangeListener) {
        variableChangeListeners.add(variableChangeListener);
    }

    protected void setDefaultValue(Variable variable, @Nullable Object defaultValue) {
        defaultValues.put(variable.getPointer(), defaultValue);
    }

    protected Collection<ReplVariableChangeListener> getVariableChangeListeners() {
        return variableChangeListeners;
    }

}
