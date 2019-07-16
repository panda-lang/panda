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

package org.panda_lang.panda.utilities.autodata.data.repository;

import org.panda_lang.panda.utilities.autodata.data.entity.EntitySchemeOperationType;

public enum RepositoryOperationType {

    CREATE(EntitySchemeOperationType.CREATE),
    DELETE(EntitySchemeOperationType.DELETE),
    UPDATE(EntitySchemeOperationType.UPDATE),
    FIND(EntitySchemeOperationType.FIND);

    private final EntitySchemeOperationType type;

    RepositoryOperationType(EntitySchemeOperationType type) {
        this.type = type;
    }

    public EntitySchemeOperationType getType() {
        return type;
    }

    public static RepositoryOperationType of(String name) {
        for (RepositoryOperationType value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }

        return null;
    }

}
