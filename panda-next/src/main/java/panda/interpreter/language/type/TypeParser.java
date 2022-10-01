package panda.interpreter.language.type;

import panda.interpreter.lexer.TokenStream;
import panda.interpreter.token.Identifier;
import panda.interpreter.token.Keyword;
import panda.interpreter.token.Section;

public class TypeParser {

    public TypeDeclaration parse(TokenStream stream) {
        var typeKeyword = stream.read(Keyword.class);
        var typeNameIdentifier = stream.read(Identifier.class);
        var body = stream.read(Section.class);

        var typeStatement = new TypeDeclaration(typeNameIdentifier.getValue());

        // read body

        return typeStatement;
    }

}
