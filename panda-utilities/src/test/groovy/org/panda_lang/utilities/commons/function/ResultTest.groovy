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

import groovy.transform.CompileStatic;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@CompileStatic
final class ResultTest {

    @Test
    void 'should map value' () {
        assertEquals(7, Result.ok("7").map(value -> Integer.parseInt(value)).get())
    }

    @Test
    void 'should return alternative result if errored' () {
        assertEquals(7, Result.error(-1).orElse(err -> Result.ok(7)).get())
    }

    @Test
    void 'should return alternative value if errored' () {
        assertEquals(7, Result.error(-1).orElseGet(err -> 7))
    }

    @Test
    void 'should evaluate error closure if errored' () {
        def integer = new AtomicInteger(-1)
        Result.error(integer.get()).onError(err -> integer.set(Math.abs(err)))
        assertEquals(1, integer.get())
    }

    @Test
    void 'should return proper ok status' () {
        assertTrue(Result.ok("ok").isOk())
        assertFalse(Result.error("err").isOk())
    }

    @Test
    void 'should return result value' () {
        assertEquals("value", Result.ok("value").get())
    }

    @Test
    void 'should return proper error status' () {
        assertTrue(Result.error("err").isErr());
        assertFalse(Result.ok("ok").isErr());
    }

    @Test
    void 'should return error value' () {
        assertEquals("err", Result.error("err").getError());
    }

    @Test
    void 'should be equal' () {
        def res1 = Result.ok("test")
        def res2 = Result.ok("another-test")
        def res3 = Result.ok("test")

        assertTrue res1 == res3
        assertFalse res1 == res2
    }

}