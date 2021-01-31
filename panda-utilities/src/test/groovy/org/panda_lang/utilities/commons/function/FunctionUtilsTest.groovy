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

import java.util.stream.Collectors

import static org.junit.jupiter.api.Assertions.assertEquals

@CompileStatic
final class FunctionUtilsTest {

    @Test
    void map() {
        List<String> data = Arrays.asList("1", "b", "2")

        List<Integer> numbers = data.stream()
                .map(FunctionUtils.map((ThrowingFunction<String, Integer, NumberFormatException>) (element -> Integer.parseInt(element.toString())), e -> -1))
                .collect(Collectors.toList())

        assertEquals(Arrays.asList(1, -1, 2), numbers)
    }

}