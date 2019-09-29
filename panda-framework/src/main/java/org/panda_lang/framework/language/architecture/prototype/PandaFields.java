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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.architecture.prototype.Fields;

import java.util.ArrayList;
import java.util.List;

final class PandaFields implements Fields {

    private final List<PrototypeField> fields = new ArrayList<>();

    @Override
    public void declare(PrototypeField field) {
        fields.add(field);
    }

    @Override
    public int getIndexOfField(PrototypeField field) {
        return fields.indexOf(field);
    }

    @Override
    public @Nullable PrototypeField getField(int fieldId) {
        for (PrototypeField field : fields) {
            if (field.getFieldIndex() == fieldId) {
                return field;
            }
        }

        return null;
    }

    @Override
    public @Nullable PrototypeField getField(String fieldName) {
        for (PrototypeField field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        return null;
    }

    @Override
    public List<? extends PrototypeField> getProperties() {
        return fields;
    }

    @Override
    public int size() {
        return fields.size();
    }

}
