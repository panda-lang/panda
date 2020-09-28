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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.utilities.commons.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PandaStreamTest {

    private static final List<String> VALUES = Arrays.asList("1", "2", "3");
    private static final Integer[] NUMBERS = { 1, 2, 3 };

    @Test
    void stream() {
        Assertions.assertEquals(1, PandaStream.of(VALUES).stream(stream -> stream.filter("2"::equals)).count());
    }

    @Test
    void map() {
        Assertions.assertArrayEquals(NUMBERS, PandaStream.of(VALUES).map(Integer::parseInt).sorted().toArray(Integer[]::new));
    }

    @Test
    void flatMap() {
        Assertions.assertArrayEquals(NUMBERS, PandaStream.of(VALUES)
                .flatMap(string -> string.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
                .map(c -> Character.toString(c))
                .map(Integer::parseInt)
                .sorted()
                .toArray(Integer[]::new));
    }

    @Test
    void filter() {
        Assertions.assertTrue(PandaStream.of(VALUES)
                .filter("2"::equals)
                .head()
                .isDefined());
    }

    @Test
    void find() {
        Assertions.assertTrue(PandaStream.of(VALUES).find("2"::equals).isDefined());
    }

    @Test
    void head() {
        Assertions.assertEquals("1", PandaStream.of(VALUES).head().get());
    }

    @Test
    void count() {
        Assertions.assertEquals(3, PandaStream.of(VALUES).count());
    }

    @Test
    void testCount() {
        Assertions.assertEquals(1, PandaStream.of(VALUES).count("2"::equals));
    }

    @Test
    void testTakeWhile() {
        Assertions.assertArrayEquals(ArrayUtils.of(1, 2), PandaStream.of(1, 2, 3, 4, 5).takeWhile(i -> i < 3).toArray(Integer[]::new));
    }

    @Test
    void toStream() {
        Assertions.assertArrayEquals(NUMBERS, PandaStream.of(VALUES).map(Integer::parseInt).toStream().sorted().toArray(Integer[]::new));
    }

    @Test
    void of() {
        Stream<String> stream = VALUES.stream();
        Assertions.assertEquals(stream, PandaStream.of(stream).toStream());
    }

}