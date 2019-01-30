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
import org.panda_lang.panda.utilities.commons.collection.Lists;

import java.util.Arrays;
import java.util.List;

class ListsTest {

    private static final List<String> LIST = Arrays.asList("a", "b", "c");

    @Test
    public void testGet() {
        Assertions.assertAll(
                () -> Assertions.assertNotNull(Lists.get(LIST, 0)),
                () -> Assertions.assertNotNull(Lists.get(LIST, 2)),

                () -> Assertions.assertNull(Lists.get(LIST, -1)),
                () -> Assertions.assertNull(Lists.get(LIST, 3))
        );
    }

}
