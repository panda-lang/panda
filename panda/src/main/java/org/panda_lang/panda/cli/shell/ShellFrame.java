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
import org.panda_lang.framework.design.architecture.prototype.PropertyFrame;
import org.panda_lang.framework.design.architecture.statement.FramedScope;

import java.util.HashMap;
import java.util.Map;

final class ShellFrame implements Frame, PropertyFrame {

    private final ShellScope scope;
    private final Frame instance;
    private final Map<Integer, Object> memory = new HashMap<>();

    protected ShellFrame(ShellScope scope, Frame instance) {
        this.scope = scope;
        this.instance = instance;
    }

    @Override
    public <T> @Nullable T set(int pointer, @Nullable T value) {
        memory.put(pointer, value);
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @Nullable T get(int pointer) {
        return (T) memory.get(pointer);
    }

    @Override
    public int getAmountOfVariables() {
        return memory.size();
    }

    @Override
    public FramedScope getScope() {
        return scope;
    }

    @Override
    public Frame getInstance() {
        return instance;
    }

}
