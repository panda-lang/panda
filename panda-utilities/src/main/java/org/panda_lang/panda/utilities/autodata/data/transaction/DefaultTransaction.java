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

package org.panda_lang.panda.utilities.autodata.data.transaction;

import org.panda_lang.panda.utilities.autodata.data.repository.DataHandler;

import java.util.Collections;

public final class DefaultTransaction {

    public static <T> DataTransaction of(DataHandler<T> handler, T entity, DataModification modification) {
        return new Transaction<>(handler, entity, null, () -> Collections.singletonList(modification))
                .retry((attempt, time) -> attempt < 10 && time < 5000);
    }

}
