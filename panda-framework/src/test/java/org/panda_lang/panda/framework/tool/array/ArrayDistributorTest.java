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

package org.panda_lang.panda.framework.tool.array;

import org.junit.Assert;
import org.junit.Test;
import org.panda_lang.panda.utilities.commons.arrays.ArrayDistributor;

public class ArrayDistributorTest {

    private static final String[] array = { "a", "b", "c", "d", "e" };

    @Test
    public void testArrayDistributor() {
        ArrayDistributor<String> distributor = new ArrayDistributor<>(array);
        distributor.setIndex(2);

        Assert.assertEquals(distributor.getPrevious(2), "a");
        Assert.assertEquals(distributor.getPrevious(), "b");

        Assert.assertEquals(distributor.current(), "c");

        Assert.assertEquals(distributor.further(), "d");
        Assert.assertEquals(distributor.future(), "e");

    }

}
