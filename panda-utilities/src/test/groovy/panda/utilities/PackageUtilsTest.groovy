/*
 * Copyright (c) 2021 dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package panda.utilities

import groovy.transform.CompileStatic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertAll
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNull

@CompileStatic
final class PackageUtilsTest {

    @Test
    void getPackageName() {
        assertAll(
                () -> assertEquals("java.lang", PackageUtils.getPackageName(String.class)),
                () -> assertNull(PackageUtils.getPackageName(void.class))
        )
    }

    @Test
    void getShortenPackage() {
        assertEquals("j.l.String", PackageUtils.getShortenPackage(String.class))
        assertEquals("j.l.String", PackageUtils.getShortenPackage(String.class.getName()))
        assertEquals("j.lang", PackageUtils.getShortenPackage(String.class.getPackage().getName()))
    }

    @Test
    void toString1() {
        assertEquals("java.lang", PackageUtils.toString(String.class.getPackage()))
        assertNull(PackageUtils.toString(int.class.getPackage()))
    }

    @Test
    void toString2() {
        assertEquals("java.lang", PackageUtils.toString(String.class.getPackage(), null))
        assertEquals("null package", PackageUtils.toString(int.class.getPackage(), "null package"))
    }

}
