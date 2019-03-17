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

package org.panda_lang.panda.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BitwiseUtilsTest {

    private static final int LEFT = 111;
    private static final int RIGHT = 222;

    private static final long EXPECTED_VALUE = 476_741_370_078L;
    private static final long VALUE = BitwiseUtils.convert(LEFT, RIGHT);

    @Test
    public void testValue() {
        Assertions.assertEquals(EXPECTED_VALUE, VALUE);
    }

    @Test
    public void extractLeft() {
        Assertions.assertEquals(LEFT, BitwiseUtils.extractLeft(VALUE));
    }

    @Test
    public void extractRight() {
        Assertions.assertEquals(RIGHT, BitwiseUtils.extractRight(VALUE));
    }

}
