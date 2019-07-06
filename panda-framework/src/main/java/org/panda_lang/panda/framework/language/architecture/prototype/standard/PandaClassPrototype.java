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

package org.panda_lang.panda.framework.language.architecture.prototype.standard;

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

public class PandaClassPrototype extends AbstractClassPrototype {

    private boolean initialized;

    protected PandaClassPrototype(Module module, String className, Class<?> associated) {
        super(module, className, associated);
    }

    protected PandaClassPrototype(PandaClassPrototypeBuilder<?, ?> builder) {
        this(builder.module, builder.name, builder.associated);
    }

    public synchronized void initialize() {
        if (initialized) {
            return;
        }

        this.initialized = true;

        for (PrototypeField field : fields.getListOfFields()) {
            if (!field.hasDefaultValue() || !field.isStatic()) {
                continue;
            }

            Expression expression = field.getDefaultValue();
            field.setStaticValue(expression.evaluate(null));
        }
    }

    public static ClassPrototypeReference of(Module module, Class<?> type, String name) {
        PandaClassPrototype prototype = builder()
                .module(module)
                .name(name)
                .associated(type)
                .build();

        return module.add(new PandaClassPrototypeReference(prototype));
    }

    public static <T> PandaClassPrototypeBuilder<?, ?> builder() {
        return new PandaClassPrototypeBuilder<>();
    }

}
