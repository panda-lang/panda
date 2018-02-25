/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.language.structure.prototype.mapper.structure;

import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.field.PrototypeField;
import org.panda_lang.panda.language.structure.prototype.structure.field.FieldVisibility;

public class ClassPrototypeFieldMapping extends PrototypeField {

    public ClassPrototypeFieldMapping(ClassPrototype type, int fieldIndex, String name, FieldVisibility visibility, boolean isStatic) {
        super(type, fieldIndex, name, visibility, isStatic, true);
    }

}
