package panda.interpreter.language.require;

import panda.interpreter.lexer.TokenStream;
import panda.interpreter.parser.Declaration.InScriptDeclaration;
import panda.interpreter.token.Identifier;
import panda.interpreter.token.Keyword;
import panda.interpreter.token.Separator.SeparatorType;
import panda.interpreter.token.TokenType;

public class ImportParser {

    public InScriptDeclaration parse(TokenStream stream) {
        var requireKeyword = stream.read(Keyword.class);
        var name = stream.read(Identifier.class);
        var fromKeyword = stream.read(Keyword.class);
        var from = stream.read(token -> token.getType() == TokenType.IDENTIFIER || token.getValue() == SeparatorType.PERIOD);

        return new ImportDeclaration(from + "." + name);
    }

}
