package org.panda_lang.panda.examples.lang

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

@CompileStatic
class MainTest extends LangTestSpecification {

    @Test
    void 'should compile and execute main statement' () {
        launch 'main.panda'
    }

}
