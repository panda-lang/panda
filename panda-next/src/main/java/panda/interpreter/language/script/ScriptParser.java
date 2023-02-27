package panda.interpreter.language.script;

import panda.interpreter.language.function.FunctionParser;
import panda.interpreter.language.require.RequireParser;
import panda.interpreter.lexer.TokenStream;
import panda.interpreter.language.type.TypeParser;
import panda.interpreter.parser.AbstractSyntaxTree;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.Declaration.InScriptDeclaration;
import panda.interpreter.source.Source;
import panda.interpreter.token.Keyword;

public final class ScriptParser {

    public static AbstractSyntaxTree parse(Context context, Source main) {
        var abstractSyntaxTree = new AbstractSyntaxTree();

        var syntaxTree = context.getLexer().tokenize(main.getContent());
        var tokenStream = new TokenStream(syntaxTree.getValue());

        while (tokenStream.hasNext()) {
            var token = tokenStream.preview();

            switch (token.getType()) {
                case INDENTATION, COMMENT -> tokenStream.read();
                case KEYWORD -> abstractSyntaxTree.addStatement(parseByKeyword(tokenStream, (Keyword) token));
                default -> throw new IllegalStateException("Illegal token in root scope: "  + token);
            }
        }

        return abstractSyntaxTree;
    }

    private static InScriptDeclaration parseByKeyword(TokenStream stream, Keyword keyword) {
        return switch (keyword.getValue()) {
            case REQUIRE -> RequireParser.parse(stream);
            case TYPE -> TypeParser.parse(stream);
            case FUN -> FunctionParser.parse(stream);
            default -> throw new IllegalStateException("Illegal keyword in root scope: " + keyword);
        };
    }

}
