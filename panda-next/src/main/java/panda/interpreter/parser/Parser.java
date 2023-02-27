package panda.interpreter.parser;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.language.script.ScriptParser;
import panda.interpreter.lexer.Lexer;
import panda.interpreter.source.Source;
import java.nio.file.Path;

public class Parser {

    public static AbstractSyntaxTree parse(@Nullable Path workingDirectory, Source mainScript) {
        var parseQueue = new ParseQueue();

        var context = new Context(
            new Lexer(),
            parseQueue
        );

        context.getParseQueue().addToQueue(
            Scope.SCRIPT,
            () -> ScriptParser.parse(context, mainScript)
        );

        try {
            while (parseQueue.parseNext()) {
                // detect infinite loop
            }
        } catch (Exception exception) {
            // error handling
            exception.printStackTrace();
            throw exception;
        }

        return null; // return sth like ApplicationDeclaration with AST+Options (main func etc)?
    }

}
