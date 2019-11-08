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

package org.panda_lang.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StackTraceUtilsTest {

    @Test
    void filter() {
        Exception exception = new Exception();
        exception.fillInStackTrace();

        StackTraceElement[] filtered = StackTraceUtils.filter(exception.getStackTrace(), StackTraceUtilsTest.class);
        Assertions.assertFalse(ArrayUtils.findIn(filtered, element -> element.getClassName().equals(StackTraceUtilsTest.class.getName())).isPresent());
    }

}