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

import java.util.Collection;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class StreamUtils {

    private StreamUtils() { }

    public static <T> long sum(Iterable<T> iterable, ToLongFunction<? super T> function) {
        return stream(iterable).mapToLong(function).sum();
    }

    public static <T> long sumLongs(Iterable<T> iterable, ToLongFunction<? super T> function) {
        return stream(iterable).mapToLong(function).sum();
    }

    public static <T> long count(Iterable<T> iterable, Predicate<T> filter) {
        return stream(iterable).filter(filter).count();
    }

    public static <T> Optional<T> findFirst(Iterable<T> iterable, Predicate<T> filter) {
        return stream(iterable).filter(filter).findFirst();
    }

    public static <R, T> Collection<R> map(Iterable<T> iterable, Function<T, R> mapper) {
        return stream(iterable).map(mapper).collect(Collectors.toList());
    }

    private static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> BinaryOperator<T> emptyBinaryOperator() {
        return (a, b) -> {
            throw new RuntimeException("Empty binary operator called by parallel stream");
        };
    }

}
