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

package org.panda_lang.panda.framework.language.architecture.prototype.clazz;

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.value.StaticValue;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;

import java.util.Collection;

public class PandaClassPrototype extends AbstractClassPrototype {

    private boolean initialized;

    protected PandaClassPrototype(Module module, String className, Class<?> associated, Collection<String> aliases) {
        super(module, className, associated, aliases);
    }

    protected PandaClassPrototype(PandaClassPrototypeBuilder<?, ?> builder) {
        this(builder.module, builder.name, builder.associated, builder.aliases);
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
            StaticValue staticValue = PandaStaticValue.of(expression.getExpressionValue(null));
            field.setStaticValue(staticValue);
        }
    }

    public static ClassPrototype of(Module module, Class<?> type, String... aliases) {
        PandaClassPrototype prototype = builder()
                .module(module)
                .associated(type)
                .aliases(aliases)
                .build();

        return module.add(prototype);
    }

    public static <T> PandaClassPrototypeBuilder<?, ?> builder() {
        return new PandaClassPrototypeBuilder<>();
    }

}
