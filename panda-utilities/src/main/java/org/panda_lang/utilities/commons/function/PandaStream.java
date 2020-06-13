/*
 * Copyright (c) 2020 Dzikoysk
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PandaStream<T> {

    private Stream<T> stream;

    private PandaStream(Stream<T> stream) {
        this.stream = stream;
    }

    public <R> PandaStream<R> stream(Function<Stream<T>, Stream<R>> function) {
        return new PandaStream<>(function.apply(stream));
    }

    public <R> PandaStream<R> map(Function<T, R> function) {
        return new PandaStream<>(stream.map(function));
    }

    public <R> PandaStream<R> flatMap(Function<T, Iterable<R>> function) {
        return new PandaStream<>(stream.flatMap(value -> StreamSupport.stream(function.apply(value).spliterator(), false)));
    }

    public PandaStream<T> filter(Predicate<T> predicate) {
        return with(stream.filter(predicate));
    }

    public PandaStream<T> sorted() {
        return with(stream.sorted());
    }

    public PandaStream<T> sorted(Comparator<? super T> comparator) {
        return with(stream.sorted(comparator));
    }

    public Option<T> find(Predicate<T> predicate) {
        return filter(predicate).head();
    }

    public Option<T> head() {
        return Option.ofOptional(stream.findFirst());
    }

    public long count(Predicate<T> predicate) {
        return filter(predicate).count();
    }

    public long count() {
        return stream.count();
    }

    private PandaStream<T> with(Stream<T> stream) {
        this.stream = stream;
        return this;
    }

    public <A, R> R collect(Collector<? super T, A, R> collector) {
        return stream.collect(collector);
    }

    public T[] toArray(IntFunction<T[]> function) {
        return stream.toArray(function);
    }

    public Stream<T> toStream() {
        return stream;
    }

    public static <T> PandaStream<T> of(Stream<T> stream) {
        return new PandaStream<>(stream);
    }

    public static <T> PandaStream<T> of(Collection<T> collection) {
        return of(collection.stream());
    }

    public static <T> PandaStream<T> of(Iterable<T> iterable) {
        return of(StreamSupport.stream(iterable.spliterator(), false));
    }

    public static <T> PandaStream<T> of(T[] array) {
        return of(Arrays.stream(array));
    }

}
