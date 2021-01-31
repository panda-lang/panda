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

package org.panda_lang.utilities.commons.iterable

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
final class CharArrayDistributorTest {

    static final String PATTERN = "abc";
    static final int ILLEGAL_INDEX_OVER_SIZE = 10;
    static final int ILLEGAL_INDEX_UNDER_ZERO = -10;

    @Test
    void next_and_previous_should_move_to_the_neighbor_element_and_return_it() {
        def charArrayDistributor = new CharArrayDistributor(PATTERN)

        assertTrue charArrayDistributor.hasNext()
        assertEquals 'a' as char, charArrayDistributor.next()
        assertEquals 'b' as char, charArrayDistributor.next()
        assertEquals 'c' as char, charArrayDistributor.next()
        assertFalse charArrayDistributor.hasNext()
        assertEquals 'c' as char, charArrayDistributor.next()

        assertEquals 'b' as char, charArrayDistributor.previous()
        assertEquals 'a' as char, charArrayDistributor.previous()
        assertEquals 'a' as char, charArrayDistributor.previous()
    }

    @Test
    void further_should_return_next_element_of_current_element() {
        def charArrayDistributor = new CharArrayDistributor(PATTERN)

        assertEquals('a' as char, charArrayDistributor.further())
        charArrayDistributor.next()
        assertEquals('b' as char, charArrayDistributor.further())
        charArrayDistributor.next()
        assertEquals('c' as char, charArrayDistributor.further())
        charArrayDistributor.next()
        assertEquals('c' as char, charArrayDistributor.further())
    }

    @Test
    void future_should_return_next_element_after_the_current_element() {
        def charArrayDistributor = new CharArrayDistributor(PATTERN)

        assertEquals('b' as char, charArrayDistributor.future())
        charArrayDistributor.next()
        assertEquals('c' as char, charArrayDistributor.future())
        charArrayDistributor.next()
        assertEquals('c' as char, charArrayDistributor.future())
    }

    @Test
    void getNext_should_return_next_element_of_current_element() {
        def charArrayDistributor = new CharArrayDistributor(PATTERN)

        assertEquals('a' as char, charArrayDistributor.getNext())
        charArrayDistributor.setIndex(ILLEGAL_INDEX_OVER_SIZE)
        assertEquals('c' as char, charArrayDistributor.getNext())
    }

    @Test
    void getPrevious_should_return_previous_element_of_current_element() {
        def charArrayDistributor = new CharArrayDistributor(PATTERN)

        assertEquals('a' as char, charArrayDistributor.getPrevious())
        charArrayDistributor.setIndex(2)
        assertEquals('b' as char, charArrayDistributor.getPrevious())
    }

    @Test
    void getPrevious_should_return_the_previous_element_by_the_specified_number() {
        def charArrayDistributor = new CharArrayDistributor(PATTERN)

        assertEquals('a' as char, charArrayDistributor.getPrevious(1))
        charArrayDistributor.setIndex(2)
        assertEquals('a' as char, charArrayDistributor.getPrevious(2))
    }

    @Test
    void previous_should_return_last_item_when_current_index_is_over_array_size() {
        def charArrayDistributor = new CharArrayDistributor(PATTERN)
        charArrayDistributor.setIndex(ILLEGAL_INDEX_OVER_SIZE)

        assertEquals('c' as char, charArrayDistributor.previous())
    }

    @Test
    void previous_should_return_first_item_when_current_index_is_under_minus_one() {
        def charArrayDistributor = new CharArrayDistributor(PATTERN)
        charArrayDistributor.setIndex(ILLEGAL_INDEX_UNDER_ZERO)

        assertEquals('a' as char, charArrayDistributor.previous())
    }

}
