package org.panda_lang.panda.examples.lang

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import org.panda_lang.panda.util.PandaUtils

@CompileStatic
class BitwiseTest {

    @Test
    void 'should compile and execute bitwise expressions' () {
        PandaUtils.load('../examples/lang', '../examples/lang/bitwise.panda')
                .flatMap({ application -> application.launch() })
                .get()
    }

}
