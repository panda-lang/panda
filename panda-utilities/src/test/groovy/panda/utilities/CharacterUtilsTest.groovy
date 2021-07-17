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
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
final class CharacterUtilsTest {

    @Test
    void testBelongsTo() {
        assertTrue(CharacterUtils.belongsTo('b' as char, "abc".toCharArray()))
        assertFalse(CharacterUtils.belongsTo('d' as char, "abc".toCharArray()))
    }

    @Test
    void testIsWhitespace() {
        assertAll(
                () -> assertTrue(CharacterUtils.isWhitespace(' ' as char)),
                () -> assertFalse(CharacterUtils.isWhitespace('a' as char)),

                () -> assertTrue(CharacterUtils.isWhitespace((char) 0)),
                () -> assertTrue(CharacterUtils.isWhitespace(CharacterUtils.TAB)),
                () -> assertTrue(CharacterUtils.isWhitespace(CharacterUtils.NO_BREAK_SPACE))
        )
    }

    @Test
    void testGetIndex() {
        assertEquals(-1, CharacterUtils.getIndex(new char[] { 'a', 'b', 'c' }, 'd' as char))
        assertEquals(1, CharacterUtils.getIndex(new char[] { 'a', 'b', 'c' }, 'b' as char))
    }

    @Test
    void testLetters() {
        assertEquals('a' as char, CharacterUtils.LETTERS[0])
        assertEquals('Z' as char, CharacterUtils.LETTERS[CharacterUtils.LETTERS.length - 1])
    }

    @Test
    void testDigits() {
        assertEquals('0' as char, CharacterUtils.DIGITS[0])
        assertEquals('9' as char, CharacterUtils.DIGITS[CharacterUtils.DIGITS.length - 1])
    }

}
