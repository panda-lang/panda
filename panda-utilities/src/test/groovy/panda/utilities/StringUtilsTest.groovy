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
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
final class StringUtilsTest {

    @Test
    void consts() {
        assertEquals(0, StringUtils.EMPTY.length())
        assertEquals(0, StringUtils.EMPTY_ARRAY.length)
    }

    @Test
    void isEmpty() {
        assertAll(
                () -> assertTrue(StringUtils.isEmpty(null)),
                () -> assertTrue(StringUtils.isEmpty(StringUtils.EMPTY)),
                () -> assertTrue(StringUtils.isEmpty("  ")),
                () -> assertFalse(StringUtils.isEmpty(" test ")),

                () -> assertEquals("abc", StringUtils.isEmpty("abc", "text is empty"))
        )

        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> StringUtils.isEmpty(StringUtils.EMPTY, "text is empty"))
        assertEquals("text is empty", throwable.getMessage())
    }

    @Test
    void replace() {
        assertAll(
                () -> assertEquals("Test", StringUtils.replace("Tests x", "s x", StringUtils.EMPTY)),
                () -> assertEquals("b b b", StringUtils.replace("a a a", "a", "b")),
                () -> assertEquals("a", StringUtils.replace("a", StringUtils.EMPTY, null))
        )
    }

    @Test
    void replaceFirst() {
        assertEquals("a b c", StringUtils.replaceFirst("b b c", "b", "a"))
        assertEquals("a b c", StringUtils.replaceFirst("a b c", "d", "e"))

        assertEquals("abc", StringUtils.replaceFirst("aac", "a", "b", 1))
        assertEquals("abc", StringUtils.replaceFirst("abc", "d", "e", 4))
    }

    @Test
    void replaceRespectively() {
        assertAll(
                () -> assertEquals("a b c", StringUtils.replaceRespectively("? ? ?", "?", "a", "b", "c")),
                () -> assertThrows(IllegalArgumentException.class, () -> StringUtils.replaceRespectively("? ?", "?", "a")),
                () -> assertEquals("a? b c", StringUtils.replaceRespectively("? b ?", "?", "a?", "c"))
        )

        Throwable amountException = assertThrows(IllegalArgumentException.class, () -> StringUtils.replaceRespectively("? ?", "?", "a"))
        assertEquals("The amount of values does not match the amount of pattern occurrences", amountException.getMessage())

        Throwable valuesException = assertThrows(IllegalArgumentException.class, () -> StringUtils.replaceRespectively("?", "?", new String[] { null }))
        assertEquals("Array of values contains null", valuesException.getMessage())
    }

    @Test
    void replaceRespectivelyAndSoftly() {
        assertAll(
                () -> assertEquals("a b c", StringUtils.replaceRespectivelyAndSoftly("? ? ?", "?", "a", "b", "c")),
                () -> assertEquals("a b ?", StringUtils.replaceRespectivelyAndSoftly("? ? ?", "?", "a", "b"))
        )
    }

    @Test
    void capitalize() {
        assertEquals(".", StringUtils.capitalize("."))
        assertEquals("Test", StringUtils.capitalize("test"))
        assertEquals(StringUtils.EMPTY, StringUtils.capitalize(StringUtils.EMPTY))
    }

    @Test
    void trimStart() {
        assertAll(
                // Without changes
                () -> assertEquals("test", StringUtils.trimStart("test")),
                () -> assertEquals("test  ", StringUtils.trimStart("test  ")),
                () -> assertEquals("", StringUtils.trimStart("")),

                // With changes
                () -> assertEquals("", StringUtils.trimStart("  ")),
                () -> assertEquals("test", StringUtils.trimStart("  test"))
        )
    }

    @Test
    void trimEnd() {
        assertAll(
                // Without changes
                () -> assertEquals("test", StringUtils.trimEnd("test")),
                () -> assertEquals("  test", StringUtils.trimEnd("  test")),
                () -> assertEquals("", StringUtils.trimEnd("")),

                // With changes
                () -> assertEquals("", StringUtils.trimEnd("  ")),
                () -> assertEquals("test", StringUtils.trimEnd("test  "))
        )
    }

    @Test
    void extractParagraph() {
        assertEquals("  ", StringUtils.extractParagraph("  test"))
    }

    @Test
    void countOccurrences() {
        assertEquals(2, StringUtils.countOccurrences(" test test ", "test"))
    }

    @Test
    void containsCharacter() {
        assertTrue(StringUtils.containsCharacter("a b c", new char[] { 'a', 'c' }))
    }

    @Test
    void containsSpecialCharacters() {
        assertTrue(StringUtils.containsSpecialCharacters("abc!@#"))
    }

    @Test
    void containsOtherCharacters() {
        assertAll(
                () -> assertTrue(StringUtils.containsOtherCharacters("abc", new char[] { 'a', 'b' })),
                () -> assertFalse(StringUtils.containsOtherCharacters("abc", new char[] { 'a', 'b', 'c' }))
        )
    }

    @Test
    void isNumber() {
        assertAll(
                () -> assertFalse(StringUtils.isNumber("test")),

                () -> assertTrue(StringUtils.isNumber("777")),
                () -> assertTrue(StringUtils.isNumber("011")),

                () -> assertTrue(StringUtils.isNumber("0.001")),
                () -> assertTrue(StringUtils.isNumber("0x001"))
        )
    }

    @Test
    void buildSpace() {
        assertEquals("  test", StringUtils.buildSpace(2) + "test")
    }

    @Test
    void lastIndexOf() {
        assertAll(
                () -> assertEquals(2, StringUtils.lastIndexOf("abc", "c", 3)),
                () -> assertEquals(-1, StringUtils.lastIndexOf("abc", "c", 1)),
                () -> assertEquals(-1, StringUtils.lastIndexOf("abc", "d", 3))
        )
    }

    @Test
    void splitFirst() {
        String[] split = StringUtils.splitFirst("aa,bb,cc", ",")
        String[] splitEmpty = StringUtils.splitFirst("aaa", ",")

        assertEquals(2, split.length)
        assertEquals(0, splitEmpty.length)

        assertAll(
                () -> assertEquals("aa", split[0]),
                () -> assertEquals("bb,cc", split[1])
        )
    }

    @Test
    void startsWith() {
        assertTrue(StringUtils.startsWith("abc", chars -> chars[0] == 'a'))
        assertFalse(StringUtils.startsWith("abc", chars -> chars[2] == 'a'))
        assertFalse(StringUtils.startsWith(StringUtils.EMPTY, chars -> true))
    }

    @Test
    void split() {
        String[] elements = StringUtils.split("aa,bb,cc", ",")
        String[] emptyElements = StringUtils.split("aa", ",")
        String[] emptyContent = StringUtils.split(",", ",")

        assertEquals(3, elements.length)
        assertEquals(1, emptyElements.length)
        assertEquals(2, emptyContent.length)

        assertAll(
                () -> assertEquals("aa", elements[0]),
                () -> assertEquals("bb", elements[1]),
                () -> assertEquals("cc", elements[2])
        )
    }

    @Test
    void lastIndexOfBefore() {
        assertEquals(1, StringUtils.lastIndexOfBefore("...", ".", 1))
        assertEquals(-1, StringUtils.lastIndexOfBefore("...", ".", 3))
    }

    @Test
    void join() {
        assertEquals("abc", StringUtils.join("a", "b", "c"))
    }

    @Test
    void toStringNullObject() {
        assertEquals("null", StringUtils.toString(null))
    }

    @Test
    void toStringObject() {
        Object object = new Object()
        assertEquals(object.toString(), StringUtils.toString(object))
    }

    @Test
    void toStringArray() {
        Object[] objects = new Object[]{new Object(), new Object()};
        assertEquals(objects[0].toString() + ", " + objects[1].toString(), StringUtils.toString(objects))
        assertEquals("", StringUtils.toString(new Object[]{}))
    }

}
