package panda.interpreter.lexer;

import panda.interpreter.token.Section;
import panda.interpreter.token.Separator;
import panda.interpreter.token.Separator.SeparatorDirection;
import panda.interpreter.token.Separator.SeparatorType;
import java.util.LinkedList;
import java.util.Stack;

public class Lexer {

    public SyntaxTree tokenize(String source) {
        var ast = new SyntaxTree(new LinkedList<>(), 0, 0);
        var sourceReader = new LexicalStream(source);

        var sections = new Stack<Section>();
        sections.push(ast);

        while (sourceReader.hasContent()) {
            var token = sourceReader.read();

            if (token instanceof Separator separator) {
                SeparatorType type = separator.getValue();

                if (type.getDirection() == SeparatorDirection.LEFT) {
                    Section section = new Section(type.getGroup(), sections.size() + 1, new LinkedList<>(), token.getLine(), token.getCaret());
                    sections.peek().addToken(section);
                    sections.push(section);
                    continue;
                }

                if (type.getDirection() == SeparatorDirection.RIGHT) {
                    var currentSection = sections.pop();

                    if (currentSection.getSurroundingSeparatorsGroup().is((group) -> group == type.getGroup())) {
                        continue;
                    }

                    throw new IllegalStateException("Illegal closing operator '" + token + "' " + token.getLocation());
                }
            }

            sections.peek().addToken(token);
        }

        System.out.println(ast.toSourceString());

        return ast;
    }

}
