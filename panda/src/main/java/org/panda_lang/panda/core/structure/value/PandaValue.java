/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.core.structure.value;

import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;

public class PandaValue implements Value {

    public static final Value NULL = new PandaValue(null, null);

    private final Object value;
    private final ClassPrototype type;

    public PandaValue(ClassPrototype type, Object value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public Object getValue() {
        return getObject();
    }

    @Override
    public Object getObject() {
        return value;
    }

    @Override
    public ClassPrototype getType() {
        return type;
    }

}
