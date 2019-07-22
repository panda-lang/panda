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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.autodata.AutomatedDataException;
import org.panda_lang.panda.utilities.autodata.data.repository.DataHandler;

import java.util.List;
import java.util.function.Supplier;

public class Transaction<T> implements DataTransaction {

    private final DataHandler<T> handler;

    protected final T entity;
    protected final Runnable transactionContent;
    protected final Supplier<List<DataModification>> modificationSupplier;
    protected DataTransactionCondition retry;
    protected DataTransactionAction success;
    protected DataTransactionAction orElse;

    public Transaction(DataHandler<T> handler, T entity, @Nullable Runnable transactionContent, Supplier<List<DataModification>> modificationSupplier) {
        this.entity = entity;
        this.handler = handler;
        this.transactionContent = transactionContent;
        this.modificationSupplier = modificationSupplier;
    }

    @Override
    public DataTransaction retry(DataTransactionCondition retry) {
        this.retry = retry;
        return this;
    }

    @Override
    public DataTransaction success(DataTransactionAction success) {
        this.success = success;
        return this;
    }

    @Override
    public DataTransaction orElse(DataTransactionAction orElse) {
        this.orElse = orElse;
        return this;
    }

    @Override
    public void commit() {
        if (transactionContent != null) {
            transactionContent.run();
        }

        try {
            handler.save(new TransactionResult<>(this));
        } catch (Exception e) {
            throw new AutomatedDataException("Cannot commit transaction", e);
        }
    }

}
