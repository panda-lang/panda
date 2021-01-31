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

package org.panda_lang.utilities.commons.function;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Completable<@NotNull T> implements Publisher<T> {

    private T value;
    private boolean ready;
    private List<Subscriber<? super T>> subscribers = new ArrayList<>(3);

    public Completable() {
        this.value = null;
    }

    public boolean isReady() {
        return ready;
    }

    public T get() {
        if (isReady()) {
            return value;
        }

        throw new IllegalStateException("Option has not been completed");
    }

    public <E extends Exception> T orThrow(Supplier<E> exception) throws E {
        if (isReady()) {
            return value;
        }

        throw exception.get();
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        if (isReady()) {
            subscriber.onComplete(get());
        }
        else {
            subscribers.add(subscriber);
        }
    }

    public Completable<T> complete(T value) {
        if (isReady()) {
            return this;
        }

        this.ready = true;
        this.value = Objects.requireNonNull(value);

        for (Subscriber<? super T> subscriber : subscribers) {
            subscriber.onComplete(value);
        }

        subscribers = null;
        return this;
    }

    public Completable<T> then(Consumer<? super T> consumer) {
        subscribe(consumer::accept);
        return this;
    }

    public <R> Completable<R> thenApply(Function<? super T, R> map) {
        Completable<R> mappedOption = new Completable<>();
        subscribe(completedValue -> mappedOption.complete(map.apply(completedValue)));
        return mappedOption;
    }

    public <R> Completable<R> thenCompose(Function<? super T, ? extends Completable<R>> map) {
        Completable<R> mappedOption = new Completable<>();
        subscribe(completedValue -> map.apply(completedValue).then(mappedOption::complete));
        return mappedOption;
    }

    public CompletableFuture<T> toFuture() {
        CompletableFuture<T> future = new CompletableFuture<>();
        then(future::complete);
        return future;
    }

    public static <T> Completable<T> completed(T value) {
        return new Completable<T>().complete(value);
    }

}
