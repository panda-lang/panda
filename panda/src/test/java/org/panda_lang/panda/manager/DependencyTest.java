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

package org.panda_lang.panda.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DependencyTest {

    private static final DependencyFactory FACTORY = new DependencyFactory();

    private static final Dependency A = FACTORY.createDependency("github:dzikoysk/panda-dependency@1.0.0");
    private static final Dependency B = FACTORY.createDependency("github:dzikoysk/panda-dependency@1.0.1");
    private static final Dependency C = FACTORY.createDependency("github:dzikoysk/panda-dependency@1.0");

    @Test
    void hasHigherVersion() {
        Assertions.assertTrue(B.hasHigherVersion(A.getVersion()));
        Assertions.assertTrue(B.hasHigherVersion(C.getVersion()));
        Assertions.assertFalse(A.hasHigherVersion(C.getVersion()));
    }

    @Test
    void getVersion() {
        Assertions.assertEquals("1.0.0", A.getVersion());
        Assertions.assertEquals("1.0", C.getVersion());
    }

    @Test
    void getName() {
        Assertions.assertEquals("panda-dependency", A.getName());
    }

    @Test
    void getScope() {
        Assertions.assertEquals("dzikoysk", A.getOwner());
    }

    @Test
    void equals() {
        Assertions.assertEquals(A, B);
    }

}