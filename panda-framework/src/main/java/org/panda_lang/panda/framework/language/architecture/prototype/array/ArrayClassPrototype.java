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
import org.panda_lang.panda.framework.language.architecture.prototype.standard.PandaClassPrototype;
import org.panda_lang.panda.framework.language.resource.PandaTypes;

public class ArrayClassPrototype extends PandaClassPrototype {

    private final Class<?> type;

    public ArrayClassPrototype(Module module, Class<?> associated, Class<?> type) {
        super(module, associated.getSimpleName(), associated);

        this.type = type;
        super.extended.add(PandaTypes.ARRAY.getReference());
    }

    @Override
    public boolean isArray() {
        return true;
    }

    public Class<?> getType() {
        return type;
    }

}
