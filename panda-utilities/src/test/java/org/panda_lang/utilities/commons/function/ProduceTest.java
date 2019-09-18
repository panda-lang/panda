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

package org.panda_lang.utilities.commons.function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

class ProduceTest {

    private static final Produce<String, String> WITH_RESULT = new Produce<>("result");
    private static final Produce<String, String> WITHOUT_RESULT = new Produce<>(() -> "error");


    @Test
    void ifPresent() {
        AtomicBoolean flag = new AtomicBoolean(false);

        WITH_RESULT.ifPresent(value -> {
            Assertions.assertEquals("result", value);
            flag.set(true);
        });

        Assertions.assertTrue(flag.get());
    }

    @Test
    void hasResult() {
        Assertions.assertTrue(WITH_RESULT.hasResult());
        Assertions.assertFalse(WITHOUT_RESULT.hasResult());
    }

    @Test
    void getError() {
        Assertions.assertEquals(WITHOUT_RESULT.getError(), "error");
        Assertions.assertNull(WITH_RESULT.getError());
    }

    @Test
    void getResult() {
        Assertions.assertEquals("result", WITH_RESULT.getResult());
        Assertions.assertNull(WITHOUT_RESULT.getResult());
    }

    @Test
    void hasError() {
        Assertions.assertFalse(WITH_RESULT.hasError());
        Assertions.assertTrue(WITHOUT_RESULT.hasError());
    }

}