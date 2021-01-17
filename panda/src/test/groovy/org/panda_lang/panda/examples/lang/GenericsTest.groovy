package org.panda_lang.panda.examples.lang

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

@CompileStatic
class GenericsTest extends LangTestSpecification {

    @Test
    void 'should compile and execute generic based source' () {
        launch 'generics.panda'
    }

}
