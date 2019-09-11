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
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.language.architecture.dynamic.AbstractLivingFrame;

import java.util.concurrent.atomic.AtomicInteger;

public class ClassPrototypeLivingFrame extends AbstractLivingFrame<ClassPrototypeFrame> {

    private static final AtomicInteger idAssigner = new AtomicInteger();

    private final int id;
    private final ClassPrototype prototype;

    public ClassPrototypeLivingFrame(ClassPrototypeFrame frame, ClassPrototype classPrototype) {
        super(frame, classPrototype.getFields().getProperties().size());

        this.id = idAssigner.getAndIncrement();
        this.prototype = classPrototype;
    }

    @Override
    public void execute(Flow flow) {
        throw new RuntimeException("Cannot execute instance");
    }

    @Override
    public String toString() {
        return prototype.getName() + "#" + String.format("%06X", id & 0xFFFFF);
    }

}
