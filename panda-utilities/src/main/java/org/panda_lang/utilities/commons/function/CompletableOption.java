package org.panda_lang.utilities.commons.function;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class CompletableOption<T> extends Option<T> implements Publisher<T> {

    private List<Subscriber<? super T>> subscribers = new ArrayList<>(3);
    private boolean ready;

    public CompletableOption() {
        super(null);
    }

    public boolean isReady() {
        return ready;
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

    public CompletableOption<T> complete(T value) {
        if (isReady()) {
            return this;
        }

        this.ready = true;
        super.value = Objects.requireNonNull(value);

        for (Subscriber<? super T> subscriber : subscribers) {
            subscriber.onComplete(value);
        }

        subscribers = null;
        return this;
    }

    public CompletableOption<T> then(Consumer<? super T> consumer) {
        subscribe(consumer::accept);
        return this;
    }

    public <R> CompletableOption<R> then(Function<? super T, R> map) {
        CompletableOption<R> mappedOption = new CompletableOption<>();
        subscribe(completedValue -> mappedOption.complete(map.apply(completedValue)));
        return mappedOption;
    }

    public CompletableFuture<T> toFuture() {
        CompletableFuture<T> future = new CompletableFuture<>();
        then(future::complete);
        return future;
    }

    public static <T> CompletableOption<T> completed(T value) {
        return new CompletableOption<T>().complete(value);
    }

}
