package org.panda_lang.panda

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import org.panda_lang.language.interpreter.logging.DefaultLogger
import org.panda_lang.language.interpreter.logging.Logger

import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
final class PandaLauncherTest {

    private StringBuilder output = new StringBuilder()
    private Logger logger = new DefaultLogger({ String message -> output.append(message) })

    @Test
    void 'should print help if args are empty' () {
        PandaLauncher.launch(() -> logger, System.in)
        assertTrue output.contains('Usage')
    }

    @Test
    void 'should parse command line parameters' () {
        PandaLauncher.launch(() -> logger, System.in, '--version')
        assertTrue output.contains(PandaConstants.VERSION)
    }

}
