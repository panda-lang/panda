package org.panda_lang.panda.examples.lang

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

@CompileStatic
class MathTest extends LangTestSpecification {

    @Test
    void 'should compile and execute math expressions' () {
        launch('math.panda')
    }

}
