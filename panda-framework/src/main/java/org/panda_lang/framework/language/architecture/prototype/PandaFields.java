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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class PandaFields extends AbstractProperties<PrototypeField> implements Fields {

    private final List<PrototypeField> fields = new ArrayList<>();

    PandaFields(Prototype prototype) {
        super(prototype);
    }

    @Override
    public void declare(PrototypeField field) {
        fields.add(field);
    }

    @Override
    public Optional<PrototypeField> getField(int index) {
        for (PrototypeField field : fields) {
            if (field.getPointer() == index) {
                return Optional.of(field);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<PrototypeField> getField(String name) {
        for (PrototypeField field : fields) {
            if (field.getName().equals(name)) {
                return Optional.of(field);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<? extends PrototypeField> getDeclaredProperties() {
        return fields;
    }

    @Override
    public int size() {
        return fields.size();
    }

}
