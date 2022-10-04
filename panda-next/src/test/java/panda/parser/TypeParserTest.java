package panda.parser;

import org.junit.jupiter.api.Test;
import panda.interpreter.language.script.ScriptParser;
import panda.interpreter.parser.Parser;
import panda.interpreter.source.Source;

public class TypeParserTest {

    // @Test
    public void shouldParseType() {
        var ast = Parser.parse(
            null,
            new Source(
                "tests / TypeParserTest",
                """
                type Panda {(
                    const panda: Panda
                )}
                """
            )
        );

        System.out.println(ast.getStatements());
    }

}
