package org.panda_lang.panda.examples.lang

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import org.panda_lang.panda.util.PandaUtils

@CompileStatic
class GenericsTest {

    @Test
    void 'should compile and execute generic based source' () {
        PandaUtils.load('../examples/lang', '../examples/lang/generics.panda')
                .flatMap({ application -> application.launch() })
                .get()
    }

}
