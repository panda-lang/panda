package org.panda_lang.utilities.commons.iterable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CharArrayDistributorTest {

    static final String PATTERN = "abc";
    static final int ILLEGAL_INDEX_OVER_SIZE = 10;
    static final int ILLEGAL_INDEX_UNDER_ZERO = -10;

    @Test
    void iterate_next_and_previous() {
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
    void further() {
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
    void future() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);

        Assertions.assertEquals('b', charArrayDistributor.future());
        charArrayDistributor.next();
        Assertions.assertEquals('c', charArrayDistributor.future());
        charArrayDistributor.next();
        Assertions.assertEquals('c', charArrayDistributor.future());
    }

    @Test
    void getNext() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);

        Assertions.assertEquals('a', charArrayDistributor.getNext());
        charArrayDistributor.setIndex(ILLEGAL_INDEX_OVER_SIZE);
        Assertions.assertEquals('c', charArrayDistributor.getNext());
    }

    @Test
    void getPrevious() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);

        Assertions.assertEquals('a', charArrayDistributor.getPrevious());
        charArrayDistributor.setIndex(2);
        Assertions.assertEquals('b', charArrayDistributor.getPrevious());
    }

    @Test
    void getPrevious_with_param() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);

        Assertions.assertEquals('a', charArrayDistributor.getPrevious(1));
        charArrayDistributor.setIndex(2);
        Assertions.assertEquals('a', charArrayDistributor.getPrevious(2));
    }

    @Test
    void illegal_index_over_array_size_and_previous() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);
        charArrayDistributor.setIndex(ILLEGAL_INDEX_OVER_SIZE);

        Assertions.assertEquals('c', charArrayDistributor.previous());
    }

    @Test
    void illegal_index_under_minus_one_and_previous() {
        CharArrayDistributor charArrayDistributor = new CharArrayDistributor(PATTERN);
        charArrayDistributor.setIndex(ILLEGAL_INDEX_UNDER_ZERO);

        Assertions.assertEquals('a', charArrayDistributor.previous());
    }
}