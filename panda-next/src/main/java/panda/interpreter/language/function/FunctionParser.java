package panda.interpreter.language.function;

import panda.interpreter.lexer.TokenStream;
import panda.interpreter.token.Identifier;
import panda.interpreter.token.Operator;
import panda.interpreter.token.Section;
import panda.interpreter.token.Separator.SeparatorType;
import panda.interpreter.token.TokenType;

public class FunctionParser {

    public static FunctionDeclaration parse(TokenStream tokenStream) {
        var functionKeyword = tokenStream.read();
        var firstIdentifier = tokenStream.read(Identifier.class);

        var isExtensionFunction = tokenStream.preview()
            .getValue()
            .equals(SeparatorType.PERIOD);

        var associatedType = isExtensionFunction
            ? firstIdentifier
            : null;

        var functionName = isExtensionFunction
            ? tokenStream.dispose().read(Identifier.class)
            : firstIdentifier;

        var parametersSection = tokenStream.read(Section.class);
        var equalOperator = tokenStream.read(Operator.class);

        var isMultilineBody = tokenStream.preview().getType() == TokenType.SECTION;

        var body = isMultilineBody
            ? tokenStream.read(Section.class)
            : tokenStream.read(token -> token.getType() != TokenType.KEYWORD); // we may need to whitelist some keywords from inline body

        // var functionDeclaration = new FunctionDeclaration();


        return null;
    }

}
