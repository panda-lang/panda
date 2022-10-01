package panda.interpreter.parser;

import panda.interpreter.language.require.ImportParser;
import panda.interpreter.lexer.Lexer;
import panda.interpreter.lexer.TokenStream;
import panda.interpreter.language.type.TypeParser;
import panda.interpreter.parser.Declaration.InScriptDeclaration;
import panda.interpreter.source.Source;
import panda.interpreter.token.Keyword;

public final class GlobalParser {

    private final Lexer lexer = new Lexer();

    public AbstractSyntaxTree parse(Source main) {
        var abstractSyntaxTree = new AbstractSyntaxTree();

        var syntaxTree = lexer.tokenize(main.getContent());
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

    private InScriptDeclaration parseByKeyword(TokenStream stream, Keyword keyword) {
        return switch (keyword.getValue()) {
            case REQUIRE -> new ImportParser().parse(stream);
            case TYPE -> new TypeParser().parse(stream);
            default -> throw new IllegalStateException("Illegal keyword in root scope: " + keyword);
        };
    }

}
