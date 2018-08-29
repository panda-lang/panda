/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.utilities.commons.arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArrayDistributorTest {

    private static final String[] array = { "a", "b", "c", "d", "e" };

    @Test
    public void testArrayDistributor() {
        ArrayDistributor<String> distributor = new ArrayDistributor<>(array);
        distributor.setIndex(2);

        Assertions.assertAll(
                () -> Assertions.assertEquals("a", distributor.getPrevious(2)),
                () -> Assertions.assertEquals("b", distributor.getPrevious()),

                () -> Assertions.assertEquals("c", distributor.current()),

                () -> Assertions.assertEquals("d", distributor.further()),
                () -> Assertions.assertEquals("e", distributor.future())
        );
    }

}
