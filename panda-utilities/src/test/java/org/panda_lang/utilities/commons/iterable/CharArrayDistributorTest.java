package org.panda_lang.utilities.commons.iterable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CharArrayDistributorTest {

    static final String PATTERN = "abc";
    static final int ILLEGAL_INDEX_OVER_SIZE = 10;
    static final int ILLEGAL_INDEX_UNDER_ZERO = -10;

    @Test
    void next_and_previous_should_move_to_the_neighbor_element_and_return_it() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);

        Assertions.assertTrue(charArrayDistributor.hasNext());
        Assertions.assertEquals('a', charArrayDistributor.next());
        Assertions.assertEquals('b', charArrayDistributor.next());
        Assertions.assertEquals('c', charArrayDistributor.next());
        Assertions.assertFalse(charArrayDistributor.hasNext());
        Assertions.assertEquals('c', charArrayDistributor.next());

        Assertions.assertEquals('b', charArrayDistributor.previous());
        Assertions.assertEquals('a', charArrayDistributor.previous());
        Assertions.assertEquals('a', charArrayDistributor.previous());
    }

    @Test
    void further_should_return_next_element_of_current_element() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);

        Assertions.assertEquals('a', charArrayDistributor.further());
        charArrayDistributor.next();
        Assertions.assertEquals('b', charArrayDistributor.further());
        charArrayDistributor.next();
        Assertions.assertEquals('c', charArrayDistributor.further());
        charArrayDistributor.next();
        Assertions.assertEquals('c', charArrayDistributor.further());
    }

    @Test
    void future_should_return_next_element_after_the_current_element() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);

        Assertions.assertEquals('b', charArrayDistributor.future());
        charArrayDistributor.next();
        Assertions.assertEquals('c', charArrayDistributor.future());
        charArrayDistributor.next();
        Assertions.assertEquals('c', charArrayDistributor.future());
    }

    @Test
    void getNext_should_return_next_element_of_current_element() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);

        Assertions.assertEquals('a', charArrayDistributor.getNext());
        charArrayDistributor.setIndex(ILLEGAL_INDEX_OVER_SIZE);
        Assertions.assertEquals('c', charArrayDistributor.getNext());
    }

    @Test
    void getPrevious_should_return_previous_element_of_current_element() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);

        Assertions.assertEquals('a', charArrayDistributor.getPrevious());
        charArrayDistributor.setIndex(2);
        Assertions.assertEquals('b', charArrayDistributor.getPrevious());
    }

    @Test
    void getPrevious_should_return_the_previous_element_by_the_specified_number() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);

        Assertions.assertEquals('a', charArrayDistributor.getPrevious(1));
        charArrayDistributor.setIndex(2);
        Assertions.assertEquals('a', charArrayDistributor.getPrevious(2));
    }

    @Test
    void previous_should_return_last_item_when_current_index_is_over_array_size() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);
        charArrayDistributor.setIndex(ILLEGAL_INDEX_OVER_SIZE);

        Assertions.assertEquals('c', charArrayDistributor.previous());
    }

    @Test
    void previous_should_return_first_item_when_current_index_is_under_minus_one() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);
        charArrayDistributor.setIndex(ILLEGAL_INDEX_UNDER_ZERO);

        Assertions.assertEquals('a', charArrayDistributor.previous());
    }
}