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

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.architecture.expression.Expression;

public class PandaPrototype extends AbstractPrototype {

    private boolean initialized;

    protected PandaPrototype(Module module, String className, Class<?> associated) {
        super(module, className, associated);
    }

    protected PandaPrototype(PandaClassPrototypeBuilder<?, ?> builder) {
        this(builder.module, builder.name, builder.associated);
    }

    public synchronized void initialize() throws Exception {
        if (initialized) {
            return;
        }

        this.initialized = true;

        for (PrototypeField field : fields.getProperties()) {
            if (!field.hasDefaultValue() || !field.isStatic()) {
                continue;
            }

            Expression expression = field.getDefaultValue();
            field.setStaticValue(expression.evaluate(null, null));
        }
    }

    public static PrototypeReference of(Module module, Class<?> type, String name) {
        PandaPrototype prototype = builder()
                .module(module)
                .name(name)
                .associated(type)
                .build();

        return module.add(new PandaPrototypeReference(prototype));
    }

    public static <T> PandaClassPrototypeBuilder<?, ?> builder() {
        return new PandaClassPrototypeBuilder<>();
    }

}
