package panda.interpreter.lexer;

import panda.interpreter.token.Section;
import panda.interpreter.token.Token;
import java.util.List;

public final class SyntaxTree extends Section {

    public SyntaxTree(List<Token<?>> tokens, int line, int caret) {
        super(null, 0, tokens, line, caret);
    }

}
