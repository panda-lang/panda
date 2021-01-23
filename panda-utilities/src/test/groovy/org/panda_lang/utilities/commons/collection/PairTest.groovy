package org.panda_lang.utilities.commons.collection

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

@CompileStatic
final class PairTest {

    @Test
    void 'should store values in key-value relation' () {
        def pair = new Pair<>('test', 7.0)
        assertEquals 'test', pair.getKey()
        assertEquals 7.0, pair.getValue()
        assertEquals  "['test': '7.0']", pair.toString()
    }

}
