package org.panda_lang.panda.examples.lang

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

@CompileStatic
class TypeTest extends LangTestSpecification {

    @Test
    void 'should handle types' () {
        launch 'type.panda'
    }

}
