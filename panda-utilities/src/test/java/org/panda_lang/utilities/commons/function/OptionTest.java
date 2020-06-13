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

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

class OptionTest {

    @Test
    void filter() {
        Assertions.assertTrue(Option.of(true).filter(value -> false).isEmpty());
    }

    @Test
    void filterNot() {
        Assertions.assertTrue(Option.of(true).filterNot(value -> true).isEmpty());
    }

    @Test
    void map() {
        Assertions.assertEquals(10, Option.of("10").map(Integer::parseInt).get());
    }

    @Test
    void flatMap() {
        Assertions.assertEquals(10, Option.of("10").flatMap(value -> Option.of(Integer.parseInt(value))).get());
    }

    @Test
    void match() {
        Assertions.assertEquals("b", Option.of("2").map(Integer::parseInt).match(
                Case.of(value -> value == 1, value -> "a"),
                Case.of(value -> value == 2, value -> "b"),
                Case.of(value -> value == 3, value -> "c")
        ).get());
    }

    @Test
    void peek() {
        AtomicBoolean called = new AtomicBoolean(false);
        Option.of(true).peek(called::set);
        Assertions.assertTrue(called.get());
    }

    @Test
    void onEmpty() {
        AtomicBoolean called = new AtomicBoolean(false);
        Option.of(null).onEmpty(() -> called.set(true));
        Assertions.assertTrue(called.get());
    }

    @Test
    void orElse() {
        Assertions.assertEquals("else", Option.none().orElse("else").get());
    }

    @Test
    void testOrElse() {
        Assertions.assertEquals("else", Option.none().orElse(Option.of("else")).get());
    }

    @Test
    void testOrElse1() {
        Assertions.assertEquals("else", Option.none().orElse(() -> Option.of("else")).get());
    }

    @Test
    void orThrow() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            Option.none().orThrow(RuntimeException::new);
        });

        Assertions.assertThrows(RuntimeException.class, () -> {
            Option.none().orThrow(() -> {
                throw new RuntimeException();
            });
        });
    }

    @Test
    void orElseGet() {
        Assertions.assertEquals("else", Option.none().orElseGet("else"));
    }

    @Test
    void testOrElseGet() {
        Assertions.assertEquals("else", Option.none().orElseGet(() -> "else"));
    }

    @Test
    void getOrNull() {
        Assertions.assertNull(Option.none().getOrNull());
    }

    @Test
    void get() {
        Assertions.assertEquals(0, Option.of(0).get());
        Assertions.assertThrows(NoSuchElementException.class, () -> Option.none().get());
    }

    @Test
    void isPresent() {
        Assertions.assertTrue(Option.of(new Object()).isPresent());
    }

    @Test
    void isDefined() {
        Assertions.assertTrue(Option.of(new Object()).isDefined());
    }

    @Test
    void isEmpty() {
        Assertions.assertTrue(Option.none().isEmpty());
    }

    @Test
    void toStream() {
        Assertions.assertEquals(10, Option.of("10").toJavaStream().mapToInt(Integer::parseInt).findAny().orElse(-1));
        Assertions.assertNotNull(Option.none().toStream().toArray(Object[]::new));
    }

    @Test
    void toOptional() {
        Assertions.assertTrue(Option.of(new Object()).toOptional().isPresent());
    }

    @Test
    void none() {
        Assertions.assertNull(Option.none().getOrNull());
    }

    @Test
    void of() {
        Assertions.assertEquals("test", Option.of("test").get());
    }

    @Test
    void ofOptional() {
        Assertions.assertTrue(Option.ofOptional(Optional.of(new Object())).isDefined());
    }

    @Test
    void when() {
        Assertions.assertTrue(Option.when(true, new Object()).isDefined());
        Assertions.assertFalse(Option.when(false, new Object()).isDefined());
    }

    @Test
    void attempt() {
        Assertions.assertTrue(Option.attempt(Exception.class, Object::new).isDefined());
        Assertions.assertTrue(Option.attempt(RuntimeException.class, () -> {
            throw new RuntimeException();
        }).isEmpty());
    }

}