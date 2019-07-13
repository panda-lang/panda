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

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class Transaction {

    private final Runnable transaction;
    private BiPredicate<Integer, Integer> retry;
    private BiConsumer<Integer, Integer> success;
    private BiConsumer<Integer, Integer> orElse;

    public Transaction(Runnable transaction) {
        this.transaction = transaction;
    }

    public Transaction retry(BiPredicate<Integer, Integer> retry) {
        this.retry = retry;
        return this;
    }

    public Transaction success(BiConsumer<Integer, Integer> success) {
        this.success = success;
        return this;
    }

    public Transaction orElse(BiConsumer<Integer, Integer> orElse) {
        this.orElse = orElse;
        return this;
    }

    public void commit() {
        // eee
    }

}
