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

package org.panda_lang.framework.language.architecture.statement;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.architecture.statement.FramedScope;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;

public abstract class AbstractFramedScope extends AbstractScope implements FramedScope {

    protected int pointers;

    protected AbstractFramedScope(@Nullable Scope parent, SourceLocation location) {
        super(null, parent, location);
    }

    protected AbstractFramedScope(SourceLocation location) {
        this(null, location);
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
    public FramedScope getFramedScope() {
        return this;
    }

}
