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

package org.panda_lang.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.utilities.commons.function.StreamUtils;

import java.util.Arrays;
import java.util.Collection;

final class StreamUtilsTest {

    private static final Collection<Integer> COLLECTION = Arrays.asList(5, 2, 5);

    @Test
    void sum() {
        Assertions.assertEquals(12, StreamUtils.sum(COLLECTION, Integer::intValue));
    }

    @Test
    void count() {
        Assertions.assertEquals(3, StreamUtils.count(COLLECTION, integer -> integer < 10));
    }

    @Test
    void findFirst() {
        Assertions.assertEquals(2, StreamUtils.findFirst(COLLECTION, integer -> integer == 2).orElse(-1).intValue());
    }

    @Test
    void sumLongs() {
        Assertions.assertEquals(10, StreamUtils.sumLongs(Arrays.asList(5L, 5L), Long::longValue));
    }

    @Test
    void map() {
        Collection<String> mapped = StreamUtils.map(COLLECTION, Long::toString);

        Assertions.assertEquals(COLLECTION.size(), mapped.size());
        Assertions.assertTrue(mapped.contains("5"));
    }

}
