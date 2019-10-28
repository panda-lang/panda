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

import org.panda_lang.framework.design.architecture.prototype.Fields;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;

import java.util.List;
import java.util.Optional;

final class PandaFields extends AbstractProperties<PrototypeField> implements Fields {

    PandaFields(Prototype prototype) {
        super(PrototypeField.class, prototype);
    }

    @Override
    public Optional<PrototypeField> getField(String name) {
        List<? extends PrototypeField> fields = getPropertiesLike(name);
        return fields.isEmpty() ? Optional.empty() : Optional.of(fields.get(0));
    }

}
