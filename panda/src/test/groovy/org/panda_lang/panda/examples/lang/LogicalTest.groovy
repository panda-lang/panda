package org.panda_lang.panda.examples.lang

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import org.panda_lang.panda.util.PandaUtils

@CompileStatic
class LogicalTest {

    @Test
    void 'should compile and execute logical expressions' () {
        PandaUtils.load('../examples/lang', '../examples/lang/logical.panda')
                .flatMap({ application -> application.launch() })
                .get()
    }

}
