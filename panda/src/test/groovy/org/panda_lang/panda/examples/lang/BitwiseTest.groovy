package org.panda_lang.panda.examples.lang

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

@CompileStatic
class BitwiseTest extends LangTestSpecification {

    @Test
    void 'should compile and execute bitwise expressions' () {
        launch 'bitwise.panda'
    }

}
