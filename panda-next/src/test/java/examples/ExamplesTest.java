package examples;

import org.junit.jupiter.api.Test;
import panda.interpreter.lexer.Lexer;
import panda.std.function.ThrowingFunction;
import panda.utilities.IOUtils;
import java.io.IOException;

public class ExamplesTest {

    @Test
    void shouldTokenizeSource() throws IOException {
        var ast = new Lexer().tokenize(getSource("/examples/concept.panda"));
    }

    private String getSource(String path) throws IOException {
        return IOUtils.convertStreamToString(ExamplesTest.class.getResourceAsStream(path)).orThrow(ThrowingFunction.identity());
    }

}
