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

package org.panda_lang.framework.language.architecture.type;

import org.panda_lang.framework.design.runtime.Process;
import org.panda_lang.framework.language.architecture.dynamic.AbstractFrame;

import java.util.concurrent.atomic.AtomicInteger;

public final class TypeFrame extends AbstractFrame<TypeScope> {

    public static final AtomicInteger ID = new AtomicInteger();

    protected final int id;
    protected final Process process;

    public TypeFrame(TypeScope scope, Process process) {
        super(scope, scope.getType().getFields().getDeclaredProperties().size());
        this.id = ID.getAndIncrement();
        this.process = process;
    }

    public Process getProcess() {
        return process;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return framedScope.getType().getSimpleName() + "#" + String.format("%06X", id & 0xFFFFF);
    }

}
