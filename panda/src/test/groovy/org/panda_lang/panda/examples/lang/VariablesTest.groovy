package org.panda_lang.panda.examples.lang

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

@CompileStatic
class VariablesTest extends LangTestSpecification {

    @Test
    void 'should compile and execute source with variables' () {
        launch 'variables.panda'
    }

}
