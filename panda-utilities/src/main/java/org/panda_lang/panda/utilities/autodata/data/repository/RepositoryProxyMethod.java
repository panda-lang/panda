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

import org.panda_lang.panda.utilities.commons.function.ThrowingFunction;

final class RepositoryProxyMethod {

    private final RepositoryOperation operationType;
    private final ThrowingFunction<Object[], Object, Exception> function;

    RepositoryProxyMethod(RepositoryOperation operationType, ThrowingFunction<Object[], Object, Exception> function) {
        this.operationType = operationType;
        this.function = function;
    }

    protected Object apply(Object[] values) throws Exception {
        return function.apply(values);
    }

    protected RepositoryOperation getOperationType() {
        return operationType;
    }

}