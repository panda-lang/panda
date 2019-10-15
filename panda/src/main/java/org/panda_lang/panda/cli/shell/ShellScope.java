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

package org.panda_lang.panda.cli.shell;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.dynamic.Frame;
import org.panda_lang.framework.design.architecture.parameter.Parameter;
import org.panda_lang.framework.design.architecture.statement.Variable;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.statement.AbstractPropertyFramedScope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class ShellScope extends AbstractPropertyFramedScope {

    private final Map<Integer, Object> defaultValues = new HashMap<>();

    ShellScope(SourceLocation location, List<Parameter> parameters) {
        super(location, parameters);
    }

    @Override
    public Frame revive(ProcessStack stack, Object instance) {
        ShellFrame frame = new ShellFrame(this, (Frame) instance);

        for (Entry<Integer, Object> entry : defaultValues.entrySet()) {
            frame.set(entry.getKey(), entry.getValue());
        }

        return frame;
    }

    protected void setDefaultValue(Variable variable, @Nullable Object defaultValue) {
        defaultValues.put(variable.getPointer(), defaultValue);
    }

}
