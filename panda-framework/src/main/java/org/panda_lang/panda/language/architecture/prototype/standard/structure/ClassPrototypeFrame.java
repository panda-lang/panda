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

package org.panda_lang.panda.language.architecture.prototype.standard.structure;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.statement.Frame;
import org.panda_lang.panda.framework.design.runtime.ProcessStack;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.architecture.dynamic.AbstractLivingFrame;
import org.panda_lang.panda.language.architecture.prototype.standard.PandaClassPrototype;
import org.panda_lang.panda.language.architecture.statement.AbstractFrame;

import java.util.concurrent.atomic.AtomicInteger;

public class ClassPrototypeFrame extends AbstractFrame implements Frame {

    private final ClassPrototype prototype;

    public ClassPrototypeFrame(ClassPrototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public ClassPrototypeLivingFrame revive(ProcessStack stack, Object instance) {
        if (prototype instanceof PandaClassPrototype) {
            ((PandaClassPrototype) prototype).initialize();
        }

        ClassPrototypeLivingFrame classInstance = new ClassPrototypeLivingFrame(this, prototype);

        for (PrototypeField field : prototype.getFields().getProperties()) {
            if (!field.hasDefaultValue() || field.isStatic()) {
                continue;
            }

            Expression expression = field.getDefaultValue();
            classInstance.set(field.getFieldIndex(), expression.evaluate(stack, classInstance));
        }

        return classInstance;
    }

    public ClassPrototype getPrototype() {
        return prototype;
    }

    public static class ClassPrototypeLivingFrame extends AbstractLivingFrame<ClassPrototypeFrame> {

        private static final AtomicInteger idAssigner = new AtomicInteger();

        private final int id;
        private final ClassPrototype prototype;

        public ClassPrototypeLivingFrame(ClassPrototypeFrame frame, ClassPrototype classPrototype) {
            super(frame, classPrototype.getFields().getProperties().size());

            this.id = idAssigner.getAndIncrement();
            this.prototype = classPrototype;
        }

        @Override
        public String toString() {
            return prototype.getName() + "#" + String.format("%06X", id & 0xFFFFF);
        }

    }

}
