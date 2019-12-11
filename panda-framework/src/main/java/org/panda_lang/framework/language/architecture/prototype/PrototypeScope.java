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
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.dynamic.AbstractFrame;
import org.panda_lang.framework.language.architecture.statement.AbstractFramedScope;

import java.util.concurrent.atomic.AtomicInteger;

public final class PrototypeScope extends AbstractFramedScope implements FramedScope {

    private final Prototype prototype;

    public PrototypeScope(SourceLocation location, Prototype prototype) {
        super(location);
        this.prototype = prototype;
    }

    @Override
    public ClassPrototypeFrame revive(ProcessStack stack, Object instance) throws Exception {
        ClassPrototypeFrame classInstance = new ClassPrototypeFrame(this, prototype);

        for (PrototypeField field : prototype.getFields().getDeclaredProperties()) {
            if (!field.hasDefaultValue()) {
                continue;
            }

            if (field.isStatic()) {
                field.fetchStaticValue(); // just init
                continue;
            }

            Expression expression = field.getDefaultValue();
            classInstance.set(field.getPointer(), expression.evaluate(stack, classInstance));
        }

        return classInstance;
    }

    public Prototype getPrototype() {
        return prototype;
    }

    public static final class ClassPrototypeFrame extends AbstractFrame<PrototypeScope> {

        private static final AtomicInteger ID = new AtomicInteger();

        private final int id;
        private final Prototype prototype;

        public ClassPrototypeFrame(PrototypeScope frame, Prototype classPrototype) {
            super(frame, classPrototype.getFields().getDeclaredProperties().size());

            this.id = ID.getAndIncrement();
            this.prototype = classPrototype;
        }

        @Override
        public String toString() {
            return prototype.getSimpleName() + "#" + String.format("%06X", id & 0xFFFFF);
        }

    }

}
