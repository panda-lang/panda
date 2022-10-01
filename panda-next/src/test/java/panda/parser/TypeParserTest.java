package panda.parser;

import org.junit.jupiter.api.Test;
import panda.interpreter.parser.GlobalParser;
import panda.interpreter.source.Source;

public class TypeParserTest {

    @Test
    public void shouldParseType() {
        var parser = new GlobalParser();

        var ast = parser.parse(new Source(
            "tests / TypeParserTest",
            """
            type Panda {(
                const panda: Panda
            )}
            """
        ));

        System.out.println(ast.getStatements());
    }

}
