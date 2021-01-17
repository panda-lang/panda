/*
 * Copyright (c) 2020 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package org.panda_lang.utilities.commons.function

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import org.panda_lang.utilities.commons.ArrayUtils

import java.util.function.IntFunction
import java.util.function.Predicate
import java.util.stream.Collectors
import java.util.stream.Stream

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
class PandaStreamTest {

    private static final List<String> VALUES = Arrays.asList("1", "2", "3")
    private static final Integer[] NUMBERS = [ 1, 2, 3 ]

    @Test
    void stream() {
        assertEquals 1, PandaStream.of(VALUES)
                .stream(stream -> stream.filter(value -> "2" == value))
                .count()
    }

    @Test
    void map() {
        assertArrayEquals NUMBERS, PandaStream.of(VALUES).map(value -> Integer.parseInt(value))
                .sorted()
                .toArray({ length -> new Integer[length] } as IntFunction)
    }

    @Test
    void flatMap() {
        assertArrayEquals NUMBERS, PandaStream.of(VALUES)
                .flatMap({ string ->
                    string.chars()
                            .mapToObj({ c -> c as char } as IntFunction)
                            .collect(Collectors.toList())
                })
                .map(c -> Character.toString(c as char))
                .map(value -> Integer.parseInt(value.toString()))
                .sorted()
                .toArray({ length -> new Integer[length] } as IntFunction)
    }

    @Test
    void filter() {
        assertTrue PandaStream.of(VALUES)
                .filter(value -> "2" == value)
                .head()
                .isDefined()
    }

    @Test
    void find() {
        assertTrue PandaStream.of(VALUES)
                .find({ value -> value == "2" } as Predicate)
                .isDefined()
    }

    @Test
    void head() {
        assertEquals "1", PandaStream.of(VALUES).head().get()
    }

    @Test
    void count() {
        assertEquals 3, PandaStream.of(VALUES).count()
    }

    @Test
    void testCount() {
        assertEquals 1, PandaStream.of(VALUES).count({ value -> value == "2" })
    }

    @Test
    void testTakeWhile() {
        assertArrayEquals ArrayUtils.of(1, 2), PandaStream.of(1, 2, 3, 4, 5)
                .takeWhile(i -> i < 3)
                .toArray({ length -> new Integer[length] } as IntFunction)
    }

    @Test
    void toStream() {
        assertArrayEquals NUMBERS, PandaStream.of(VALUES)
                .map(value -> Integer.parseInt(value))
                .toStream()
                .sorted()
                .toArray({ length -> new Integer[length] } as IntFunction)
    }

    @Test
    void of() {
        Stream<String> stream = VALUES.stream()
        assertEquals stream, PandaStream.of(stream).toStream()
    }

}