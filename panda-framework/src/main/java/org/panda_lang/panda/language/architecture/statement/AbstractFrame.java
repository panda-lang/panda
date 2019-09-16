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

package org.panda_lang.panda.language.architecture.statement;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Frame;
import org.panda_lang.panda.framework.design.architecture.dynamic.Scope;

public abstract class AbstractFrame extends AbstractScope implements Frame {

    protected int pointers;

    protected AbstractFrame(@Nullable Scope parent) {
        super(null, parent);
    }

    protected AbstractFrame() {
        this(null);
    }

    @Override
    public int allocate() {
        return pointers++;
    }

    @Override
    public int getRequiredMemorySize() {
        return pointers;
    }

    @Override
    public Frame getFrame() {
        return this;
    }

}
