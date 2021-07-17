/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.architecture.type.member.field;

import org.panda_lang.framework.architecture.type.Type;
import org.panda_lang.framework.architecture.type.member.AbstractMembers;
import panda.std.Option;

import java.util.List;

public final class PandaFields extends AbstractMembers<TypeField> implements Fields {

    public PandaFields(Type type) {
        super(TypeField.class, type);
    }

    @Override
    public Option<TypeField> getField(String name) {
        List<? extends TypeField> fields = getPropertiesLike(name);
        return fields.isEmpty() ? Option.none() : Option.of(fields.get(0));
    }

}
