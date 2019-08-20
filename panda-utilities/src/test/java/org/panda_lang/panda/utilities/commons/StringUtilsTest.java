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

final class StringUtilsTest {

    @Test
    void consts() {
        Assertions.assertEquals(0, StringUtils.EMPTY.length());
        Assertions.assertEquals(0, StringUtils.EMPTY_ARRAY.length);
    }

    @Test
    void isEmpty() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(StringUtils.isEmpty(null)),
                () -> Assertions.assertTrue(StringUtils.isEmpty(StringUtils.EMPTY)),
                () -> Assertions.assertTrue(StringUtils.isEmpty("  ")),
                () -> Assertions.assertFalse(StringUtils.isEmpty(" test ")),

                () -> Assertions.assertEquals("abc", StringUtils.isEmpty("abc", "text is empty"))
        );

        Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.isEmpty(StringUtils.EMPTY, "text is empty"));
        Assertions.assertEquals("text is empty", throwable.getMessage());
    }

    @Test
    void replace() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("Test", StringUtils.replace("Tests x", "s x", StringUtils.EMPTY)),
                () -> Assertions.assertEquals("b b b", StringUtils.replace("a a a", "a", "b")),
                () -> Assertions.assertEquals("a", StringUtils.replace("a", StringUtils.EMPTY, null))
        );
    }

    @Test
    void replaceFirst() {
        Assertions.assertEquals("a b c", StringUtils.replaceFirst("b b c", "b", "a"));
        Assertions.assertEquals("a b c", StringUtils.replaceFirst("a b c", "d", "e"));

        Assertions.assertEquals("abc", StringUtils.replaceFirst("aac", "a", "b", 1));
        Assertions.assertEquals("abc", StringUtils.replaceFirst("abc", "d", "e", 4));
    }

    @Test
    void replaceRespectively() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("a b c", StringUtils.replaceRespectively("? ? ?", "?", "a", "b", "c")),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.replaceRespectively("? ?", "?", "a")),
                () -> Assertions.assertEquals("a? b c", StringUtils.replaceRespectively("? b ?", "?", "a?", "c"))
        );

        Throwable amountException = Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.replaceRespectively("? ?", "?", "a"));
        Assertions.assertEquals("The amount of values does not match the amount of pattern occurrences", amountException.getMessage());

        Throwable valuesException = Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.replaceRespectively("?", "?", new String[] { null }));
        Assertions.assertEquals("Array of values contains null", valuesException.getMessage());
    }

    @Test
    void replaceRespectivelyAndSoftly() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("a b c", StringUtils.replaceRespectivelyAndSoftly("? ? ?", "?", "a", "b", "c")),
                () -> Assertions.assertEquals("a b ?", StringUtils.replaceRespectivelyAndSoftly("? ? ?", "?", "a", "b"))
        );
    }

    @Test
    void capitalize() {
        Assertions.assertEquals(".", StringUtils.capitalize("."));
        Assertions.assertEquals("Test", StringUtils.capitalize("test"));
        Assertions.assertEquals(StringUtils.EMPTY, StringUtils.capitalize(StringUtils.EMPTY));
    }

    @Test
    void trimStart() {
        Assertions.assertAll(
                // Without changes
                () -> Assertions.assertEquals("test", StringUtils.trimStart("test")),
                () -> Assertions.assertEquals("test  ", StringUtils.trimStart("test  ")),
                () -> Assertions.assertEquals("", StringUtils.trimStart("")),

                // With changes
                () -> Assertions.assertEquals("", StringUtils.trimStart("  ")),
                () -> Assertions.assertEquals("test", StringUtils.trimStart("  test"))
        );
    }

    @Test
    void trimEnd() {
        Assertions.assertAll(
                // Without changes
                () -> Assertions.assertEquals("test", StringUtils.trimEnd("test")),
                () -> Assertions.assertEquals("  test", StringUtils.trimEnd("  test")),
                () -> Assertions.assertEquals("", StringUtils.trimEnd("")),

                // With changes
                () -> Assertions.assertEquals("", StringUtils.trimEnd("  ")),
                () -> Assertions.assertEquals("test", StringUtils.trimEnd("test  "))
        );
    }

    @Test
    void extractParagraph() {
        Assertions.assertEquals("  ", StringUtils.extractParagraph("  test"));
    }

    @Test
    void countOccurrences() {
        Assertions.assertEquals(2, StringUtils.countOccurrences(" test test ", "test"));
    }

    @Test
    void containsCharacter() {
        Assertions.assertTrue(StringUtils.containsCharacter("abcd", 'a', 'c'));
    }

    @Test
    void containsSpecialCharacters() {
        Assertions.assertTrue(StringUtils.containsSpecialCharacters("abc!@#"));
    }

    @Test
    void containsOtherCharacters() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(StringUtils.containsOtherCharacters("abc", new char[] { 'a', 'b' })),
                () -> Assertions.assertFalse(StringUtils.containsOtherCharacters("abc", new char[] { 'a', 'b', 'c' }))
        );
    }

    @Test
    void isNumber() {
        Assertions.assertAll(
                () -> Assertions.assertFalse(StringUtils.isNumber("test")),

                () -> Assertions.assertTrue(StringUtils.isNumber("777")),
                () -> Assertions.assertTrue(StringUtils.isNumber("011")),

                () -> Assertions.assertTrue(StringUtils.isNumber("0.001")),
                () -> Assertions.assertTrue(StringUtils.isNumber("0x001"))
        );
    }

    @Test
    void buildSpace() {
        Assertions.assertEquals("  test", StringUtils.buildSpace(2) + "test");
    }

    @Test
    void lastIndexOf() {
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, StringUtils.lastIndexOf("abc", "c", 3)),
                () -> Assertions.assertEquals(-1, StringUtils.lastIndexOf("abc", "c", 1)),
                () -> Assertions.assertEquals(-1, StringUtils.lastIndexOf("abc", "d", 3))
        );
    }

    @Test
    void splitFirst() {
        String[] split = StringUtils.splitFirst("aa,bb,cc", ",");
        String[] splitEmpty = StringUtils.splitFirst("aaa", ",");

        Assertions.assertEquals(2, split.length);
        Assertions.assertEquals(0, splitEmpty.length);

        Assertions.assertAll(
                () -> Assertions.assertEquals("aa", split[0]),
                () -> Assertions.assertEquals("bb,cc", split[1])
        );
    }

    @Test
    void startsWith() {
        Assertions.assertTrue(StringUtils.startsWith("abc", chars -> chars[0] == 'a'));
        Assertions.assertFalse(StringUtils.startsWith("abc", chars -> chars[2] == 'a'));
        Assertions.assertFalse(StringUtils.startsWith(StringUtils.EMPTY, chars -> true));
    }

    @Test
    void split() {
        String[] elements = StringUtils.split("aa,bb,cc", ",");
        String[] emptyElements = StringUtils.split("aa", ",");
        String[] emptyContent = StringUtils.split(",", ",");

        Assertions.assertEquals(3, elements.length);
        Assertions.assertEquals(1, emptyElements.length);
        Assertions.assertEquals(2, emptyContent.length);

        Assertions.assertAll(
                () -> Assertions.assertEquals("aa", elements[0]),
                () -> Assertions.assertEquals("bb", elements[1]),
                () -> Assertions.assertEquals("cc", elements[2])
        );
    }

    @Test
    void lastIndexOfBefore() {
        Assertions.assertEquals(1, StringUtils.lastIndexOfBefore("...", ".", 1));
        Assertions.assertEquals(-1, StringUtils.lastIndexOfBefore("...", ".", 3));
    }

}
