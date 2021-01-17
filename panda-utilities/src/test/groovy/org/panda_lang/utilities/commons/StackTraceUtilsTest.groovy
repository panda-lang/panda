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

package org.panda_lang.utilities.commons

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertFalse

@CompileStatic
class StackTraceUtilsTest {

    private static final  Exception EXCEPTION = new Exception()

    static {
        EXCEPTION.fillInStackTrace()
    }

    @Test
    void filterClass() {
        StackTraceElement[] filtered = StackTraceUtils.filter(EXCEPTION.getStackTrace(), StackTraceUtilsTest.class);
        assertFalse ArrayUtils.findIn(filtered, element -> element.getClassName().equals(StackTraceUtilsTest.class.getName())).isPresent();
    }

    @Test
    void filterString() {
        StackTraceElement[] filtered = StackTraceUtils.filter(EXCEPTION.getStackTrace(), "org.panda_lang");
        assertFalse ArrayUtils.findIn(filtered, element -> element.getClassName().contains("org.panda_lang")).isPresent();
    }

}