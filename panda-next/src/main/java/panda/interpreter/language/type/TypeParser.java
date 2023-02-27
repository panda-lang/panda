package panda.interpreter.language.type;

import panda.interpreter.language.function.FunctionParser;
import panda.interpreter.lexer.TokenStream;
import panda.interpreter.token.Identifier;
import panda.interpreter.token.Keyword;
import panda.interpreter.token.Keyword.KeywordType;
import panda.interpreter.token.Section;

public class TypeParser {

    public static TypeDeclaration parse(TokenStream stream) {
        var typeKeyword = stream.read(Keyword.class);

        var typeNameIdentifier = stream.read(Identifier.class);
        var typeStatement = new TypeDeclaration(typeNameIdentifier.getValue());

        var body = stream.read(Section.class);
        var bodyStream = new TokenStream(body.getValue());

        while (bodyStream.hasNext()) {
            var currentToken = bodyStream.preview();

            if (currentToken.getValue() == KeywordType.FUN) {
                var functionDeclaration = FunctionParser.parse(bodyStream);
                typeStatement.addFunction(functionDeclaration);
                continue;
            }

            throw new IllegalStateException("Unknown token in type scope: " + currentToken);
        }

        return typeStatement;
    }

}
