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

import java.util.List;
import java.util.Optional;

final class TransactionResult<T> implements DataTransactionResult<T> {

    private final Transaction<T> transaction;

    TransactionResult(Transaction<T> transaction) {
        this.transaction = transaction;
    }

    @Override
    public Optional<DataTransactionCondition> getRetryAction() {
        return Optional.ofNullable(transaction.retry);
    }

    @Override
    public Optional<DataTransactionAction> getSuccessAction() {
        return Optional.ofNullable(transaction.success);
    }

    @Override
    public Optional<DataTransactionAction> getElseAction() {
        return Optional.ofNullable(transaction.orElse);
    }

    @Override
    public List<? extends DataModification> getModifications() {
        return transaction.modificationSupplier.get();
    }

    @Override
    public T getEntity() {
        return transaction.entity;
    }

}
