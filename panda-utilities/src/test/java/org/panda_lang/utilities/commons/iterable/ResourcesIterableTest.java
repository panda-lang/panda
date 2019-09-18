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

package org.panda_lang.utilities.commons.iterable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.utilities.commons.collection.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

final class ResourcesIterableTest {

    @Test
    void testIterable() {
        Collection<String> mutable = Lists.mutableOf("a");
        Collection<String> immutable = Collections.singletonList("c");
        ResourcesIterable<String> iterable = new ResourcesIterable<>(mutable, immutable);

        mutable.add("b");
        Iterator<String> iterator = iterable.iterator();

        Assertions.assertEquals("a", iterator.next());
        Assertions.assertEquals("b", iterator.next());
        Assertions.assertEquals("c", iterator.next());
    }

}
