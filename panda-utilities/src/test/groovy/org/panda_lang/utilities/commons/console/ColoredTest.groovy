package org.panda_lang.utilities.commons.console;

import groovy.transform.CompileStatic;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

@CompileStatic
final class ColoredTest {

    @Test
    void toString__text_null__without_effect() {
        assertEquals MessageFormat.format("null{0}", Effect.RESET),  Colored.on(null).toString()
    }

    @Test
    void toString__text_NOT_null__with_effects() {
        Colored colored = Colored.on("line1" + System.lineSeparator() + "line2")
        colored.effect(Effect.BLACK)
        colored.effect(Effect.BOLD)

        String expected = MessageFormat.format("{0}{1}line1{2}", Effect.BLACK, Effect.BOLD, Effect.RESET)
        expected += MessageFormat.format("{0}{1}line2{2}", Effect.BLACK, Effect.BOLD, Effect.RESET)

        assertEquals expected, colored.toString()
    }

}