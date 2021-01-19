package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.number

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
final class NumberUtilsTest {

    @Test
    void 'should return true for valid number formats' () {
        assertTrue NumberUtils.isNumeric('1')
        assertTrue NumberUtils.isNumeric('-1')
        assertTrue NumberUtils.isNumeric('1.0')
        assertTrue NumberUtils.isNumeric('1_000')
        assertTrue NumberUtils.isNumeric('0x1')
        assertTrue NumberUtils.isNumeric('0xCAFEBABE')
    }

}
