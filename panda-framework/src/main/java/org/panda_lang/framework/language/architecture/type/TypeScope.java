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

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeField;
import org.panda_lang.framework.design.architecture.statement.FramedScope;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.runtime.Process;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.dynamic.AbstractFrame;
import org.panda_lang.framework.language.architecture.statement.AbstractFramedScope;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicInteger;

public final class TypeScope extends AbstractFramedScope implements FramedScope {

    private final Type type;

    public TypeScope(SourceLocation location, Type type) {
        super(location);
        this.type = type;
    }

    @Override
    public TypeFrame revive(ProcessStack stack, Object instance) throws Exception {
        TypeFrame classFrame = getConstructor().newInstance(this, stack.getProcess());

        for (TypeField field : type.getFields().getDeclaredProperties()) {
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
    private Constructor<? extends TypeFrame> getConstructor() {
        try {
            return (Constructor<? extends TypeFrame>) type.getAssociatedClass().fetchImplementation().getConstructor(TypeScope.class, Process.class);
        } catch (NoSuchMethodException e) {
            throw new PandaFrameworkException("Class " + type.getAssociatedClass().fetchImplementation() + " does not implement TypeClass constructor");
        }
    }

    public Type getType() {
        return type;
    }

    public abstract static class TypeFrame extends AbstractFrame<TypeScope> implements TypeClass {

        private static final AtomicInteger ID = new AtomicInteger();

        private final int id;
        private final Process process;

        public TypeFrame(TypeScope scope, Process process) {
            super(scope, scope.getType().getFields().getDeclaredProperties().size());
            this.id = ID.getAndIncrement();
            this.process = process;
        }

        @Override
        public Process _panda_get_process() {
            return process;
        }

        @Override
        public String toString() {
            return frame.getType().getSimpleName() + "#" + String.format("%06X", id & 0xFFFFF);
        }

    }

}
