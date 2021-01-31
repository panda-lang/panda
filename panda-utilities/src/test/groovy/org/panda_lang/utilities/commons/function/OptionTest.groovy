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

package org.panda_lang.utilities.commons.function

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.IntFunction
import java.util.function.Supplier

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
final class OptionTest {

    @Test
    void filter() {
        assertTrue Option.of(true).filter(value -> false).isEmpty()
    }

    @Test
    void filterNot() {
        assertTrue Option.of(true).filterNot(value -> true).isEmpty()
    }

    @Test
    void map() {
        assertEquals 10, Option.of("10").map(value -> Integer.parseInt(value)).get()
    }

    @Test
    void flatMap() {
        assertEquals 10, Option.of("10").flatMap(value -> Option.of(Integer.parseInt(value))).get()
    }

    @Test
    void match() {
        assertEquals "b", Option.of("2")
                .map(value -> Integer.parseInt(value))
                .match([
                    Case.of({ value -> value == 1 }, { value -> "a" }),
                    Case.of({ value -> value == 2 }, { value -> "b" }),
                    Case.of({ value -> value == 3 }, { value -> "c" })
                ] as List<Case<Integer, String>>)
                .get()
    }

    @Test
    void peek() {
        AtomicBoolean called = new AtomicBoolean(false)
        Option.of(true).peek(value -> called.set(value))
        assertTrue called.get()
    }

    @Test
    void onEmpty() {
        AtomicBoolean called = new AtomicBoolean(false)
        Option.of(null).onEmpty(() -> called.set(true))
        assertTrue called.get()
    }

    @Test
    void orElse() {
        assertEquals "else", Option.none().orElse("else").get()
    }

    @Test
    void testOrElse() {
        assertEquals "else", Option.none().orElse(Option.of("else")).get()
    }

    @Test
    void testOrElse1() {
        assertEquals "else", Option.none().orElse({ Option.of("else") } as Supplier).get()
    }

    @Test
    void orThrow() {
        assertThrows RuntimeException.class, () -> {
            Option.none().orThrow(() -> {
                throw new RuntimeException()
            })
        }

        assertThrows RuntimeException.class, () -> {
            Option.none().orThrow(() -> {
                throw new RuntimeException()
            })
        }
    }

    @Test
    void orElseGet() {
        assertEquals "else", Option.none().orElseGet("else")
    }

    @Test
    void testOrElseGet() {
        assertEquals "else", Option.none().orElseGet({ "else" } as Supplier)
    }

    @Test
    void getOrNull() {
        assertNull Option.none().getOrNull()
    }

    @Test
    void get() {
        assertEquals 0, Option.of(0).get()
        assertThrows NoSuchElementException.class, () -> Option.none().get()
    }

    @Test
    void isPresent() {
        assertTrue Option.of(new Object()).isPresent()
    }

    @Test
    void isDefined() {
        assertTrue Option.of(new Object()).isDefined()
    }

    @Test
    void isEmpty() {
        assertTrue Option.none().isEmpty()
    }

    @Test
    void toStream() {
        assertEquals 10, Option.of("10").toJavaStream().mapToInt(value -> Integer.parseInt(value)).findAny().orElse(-1)
        assertNotNull Option.<String> none().toStream().toArray({ length -> new Object[length] } as IntFunction)
    }

    @Test
    void toOptional() {
        assertTrue Option.of(new Object()).toOptional().isPresent()
    }

    @Test
    void none() {
        assertNull Option.none().getOrNull()
    }

    @Test
    void of() {
        assertEquals "test", Option.of("test").get()
    }

    @Test
    void ofOptional() {
        assertTrue Option.ofOptional(Optional.of(new Object())).isDefined()
    }

    @Test
    void when() {
        assertTrue Option.when(true, new Object()).isDefined()
        assertFalse Option.when(false, new Object()).isDefined()
    }

    @Test
    void attempt() {
        assertTrue Option.attempt(Exception.class, { new Object() }).isDefined()
        assertTrue Option.attempt(RuntimeException.class, {
            throw new RuntimeException()
        } as ThrowingSupplier).isEmpty()
    }

}