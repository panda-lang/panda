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

package org.panda_lang.panda.manager

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
final class DependencyTest {

    private static final Dependency A = Dependency.createDependency("github:dzikoysk/panda-dependency@1.0.0")
    private static final Dependency B = Dependency.createDependency("github:dzikoysk/panda-dependency@1.0.1")
    private static final Dependency C = Dependency.createDependency("github:dzikoysk/panda-dependency@1.0")

    @Test
    void hasHigherVersion() {
        assertTrue B.hasHigherVersion(A.getVersion())
        assertTrue B.hasHigherVersion(C.getVersion())
        assertFalse A.hasHigherVersion(C.getVersion())
    }

    @Test
    void getVersion() {
        assertEquals "1.0.0", A.getVersion()
        assertEquals "1.0", C.getVersion()
    }

    @Test
    void getName() {
        assertEquals "panda-dependency", A.getName()
    }

    @Test
    void getScope() {
        assertEquals "dzikoysk", A.getAuthor()
    }

    @Test
    void 'same should ignore version'() {
        assertTrue A.same(B)
    }

}