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

package panda.interpreter.architecture.type;

import panda.interpreter.runtime.Process;
import panda.interpreter.architecture.dynamic.AbstractFrame;

import java.util.concurrent.atomic.AtomicInteger;

public final class TypeFrame extends AbstractFrame<TypeScope> {

    public static final AtomicInteger ID = new AtomicInteger();

    protected final int id;
    protected final Process process;

    protected TypeInstance typeInstance;
    protected Object[] baseArguments;

    public TypeFrame(Process process, TypeScope scope) {
        super(scope, scope.getReference().fetchType().getFields().getProperties().size());

        this.id = ID.getAndIncrement();
        this.process = process;
    }

    public void setTypeInstance(TypeInstance typeInstance) {
        this.typeInstance = typeInstance;
    }

    public TypeInstance getTypeInstance() {
        return typeInstance;
    }

    public void setBaseArguments(Object[] baseArguments) {
        this.baseArguments = baseArguments;
    }

    public Object[] getBaseArguments() {
        return baseArguments;
    }

    public Process getProcess() {
        return process;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return framedScope.getReference().getSimpleName() + "#" + String.format("%06X", id & 0xFFFFF);
    }

}
