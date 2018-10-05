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

package org.panda_lang.panda.utilities.commons.objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {

    @Test
    public void testIsEmpty() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(StringUtils.isEmpty(null)),
                () -> Assertions.assertTrue(StringUtils.isEmpty("")),
                () -> Assertions.assertTrue(StringUtils.isEmpty("  ")),
                () -> Assertions.assertFalse(StringUtils.isEmpty(" test "))
        );
    }

    @Test
    public void testReplace() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("Test", StringUtils.replace("Tests x", "s x", "")),
                () -> Assertions.assertEquals("b b b", StringUtils.replace("a a a", "a", "b"))
        );
    }

    @Test
    public void testReplaceFist() {
        Assertions.assertEquals("a b c", StringUtils.replaceFirst("b b c", "b", "a"));
    }

    @Test
    public void testReplaceRespectively() {
        Assertions.assertAll(
                // Hard
                () -> Assertions.assertEquals("a b c", StringUtils.replaceRespectively("? ? ?", "?", "a", "b", "c")),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.replaceRespectively("? ?", "?", "a")),

                // Soft
                () -> Assertions.assertEquals("a b c", StringUtils.replaceRespectivelyAndSoftly("? ? ?", "?", "a", "b", "c")),
                () -> Assertions.assertEquals("a b ?", StringUtils.replaceRespectivelyAndSoftly("? ? ?", "?", "a", "b"))
        );
    }

    @Test
    public void testCapitalize() {
        Assertions.assertEquals("Test", StringUtils.capitalize("test"));
    }

    @Test
    public void testTrimStart() {
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
    public void testTrimEnd() {
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
    public void testExtractParagraph() {
        Assertions.assertEquals("  ", StringUtils.extractParagraph("  test"));
    }

    @Test
    public void testCountOccurrences() {
        Assertions.assertEquals(2, StringUtils.countOccurrences(" test test ", "test"));
    }

    @Test
    public void testContainsCharacter() {
        Assertions.assertTrue(StringUtils.containsCharacter("abcd", 'a', 'c'));
    }

    @Test
    public void testContainsSpecialCharacters() {
        Assertions.assertTrue(StringUtils.containsSpecialCharacters("abc!@#"));
    }

    @Test
    public void testIsNumber() {
        Assertions.assertAll(
                () -> Assertions.assertFalse(StringUtils.isNumber("test")),

                () -> Assertions.assertTrue(StringUtils.isNumber("777")),
                () -> Assertions.assertTrue(StringUtils.isNumber("011")),

                () -> Assertions.assertTrue(StringUtils.isNumber("0.001")),
                () -> Assertions.assertTrue(StringUtils.isNumber("0x001"))
        );
    }

    @Test
    public void testCreateIndentation() {
        Assertions.assertEquals("  test", StringUtils.createIndentation(2) + "test");
    }

    @Test
    public void testLastIndexOf() {
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, StringUtils.lastIndexOf("abc", "c", 3)),
                () -> Assertions.assertEquals(-1, StringUtils.lastIndexOf("abc", "c", 1)),
                () -> Assertions.assertEquals(-1, StringUtils.lastIndexOf("abc", "d", 3))
        );
    }

}
