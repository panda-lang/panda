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

package org.panda_lang.panda.framework.language.architecture.prototype.array;

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.language.architecture.module.PrimitivePrototypeLiquid;
import org.panda_lang.panda.framework.language.architecture.prototype.clazz.PandaClassPrototype;

import java.util.ArrayList;

public class ArrayClassPrototype extends PandaClassPrototype {

    private final Class<?> type;

    public ArrayClassPrototype(Module module, Class<?> associated, Class<?> type) {
        super(module, associated.getSimpleName(), associated, new ArrayList<>());

        this.type = type;
        super.extended.add(PrimitivePrototypeLiquid.ARRAY);
    }

    public Class<?> getType() {
        return type;
    }

}
