package org.panda_lang.panda.examples.lang

import groovy.transform.CompileStatic
import org.panda_lang.panda.util.PandaUtils

@CompileStatic
class LangTestSpecification {

    protected static void launch (String script) {
        PandaUtils.load('../examples/lang', '../examples/lang/' + script)
                .flatMap({ application -> application.launch() })
                .get()
    }

}
