package org.panda_lang.panda.examples.lang

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

@CompileStatic
class CreaseTest extends LangTestSpecification {

    @Test
    void 'should compile and execute crease operations' () {
        launch 'crease.panda'
    }

}
