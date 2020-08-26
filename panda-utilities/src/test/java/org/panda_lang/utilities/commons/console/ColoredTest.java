package org.panda_lang.utilities.commons.console;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

class ColoredTest {

    @Test
    void toString__text_null__without_effect() {
        Colored colored = Colored.on(null);

        Assertions.assertEquals(
                MessageFormat.format("null{0}", Effect.RESET),
                colored.toString()
        );
    }

    @Test
    void toString__text_NOT_null__with_effects() {
        Colored colored = Colored.on("line1" + System.lineSeparator() + "line2");
        colored.effect(Effect.BLACK);
        colored.effect(Effect.BOLD);

        String expected = MessageFormat.format("{0}{1}line1{2}", Effect.BLACK, Effect.BOLD, Effect.RESET)
                + MessageFormat.format("{0}{1}line2{2}", Effect.BLACK, Effect.BOLD, Effect.RESET);
        Assertions.assertEquals(
                expected,
                colored.toString()
        );
    }
}