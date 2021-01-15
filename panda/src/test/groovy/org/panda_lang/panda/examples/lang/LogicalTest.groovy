package org.panda_lang.panda.examples.lang

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

@CompileStatic
class LogicalTest extends LangTestSpecification {

    @Test
    void 'should compile and execute logical expressions' () {
        launch('logical.panda')
    }

}
