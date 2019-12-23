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

package org.panda_lang.framework.language.architecture.prototype;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.architecture.statement.FramedScope;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.runtime.Process;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.dynamic.AbstractFrame;
import org.panda_lang.framework.language.architecture.statement.AbstractFramedScope;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicInteger;

public final class PrototypeScope extends AbstractFramedScope implements FramedScope {

    private final Prototype prototype;

    public PrototypeScope(SourceLocation location, Prototype prototype) {
        super(location);
        this.prototype = prototype;
    }

    @Override
    public PrototypeFrame revive(ProcessStack stack, Object instance) throws Exception {
        PrototypeFrame classFrame = getConstructor().newInstance(this, stack.getProcess());

        for (PrototypeField field : prototype.getFields().getDeclaredProperties()) {
            if (!field.hasDefaultValue()) {
                continue;
            }

            if (field.isStatic()) {
                field.fetchStaticValue(); // just init
                continue;
            }

            Expression expression = field.getDefaultValue();
            classFrame.set(field.getPointer(), expression.evaluate(stack, classFrame));
        }

        return classFrame;
    }

    @SuppressWarnings("unchecked")
    private Constructor<? extends PrototypeFrame> getConstructor() throws NoSuchMethodException {
        return (Constructor<? extends PrototypeFrame>) prototype.getAssociatedClass().getImplementation().getConstructor(PrototypeScope.class, Process.class);
    }

    public Prototype getPrototype() {
        return prototype;
    }

    public abstract static class PrototypeFrame extends AbstractFrame<PrototypeScope> {

        private static final AtomicInteger ID = new AtomicInteger();

        private final int id;
        private final Process process;

        public PrototypeFrame(PrototypeScope scope, Process process) {
            super(scope, scope.getPrototype().getFields().getDeclaredProperties().size());
            this.id = ID.getAndIncrement();
            this.process = process;
        }

        public Process getProcess() {
            return process;
        }

        @Override
        public String toString() {
            return frame.getPrototype().getSimpleName() + "#" + String.format("%06X", id & 0xFFFFF);
        }

    }

}
