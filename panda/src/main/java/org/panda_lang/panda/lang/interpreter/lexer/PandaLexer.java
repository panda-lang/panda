package org.panda_lang.panda.lang.interpreter.lexer;

import org.panda_lang.core.interpreter.parser.lexer.Lexer;
import org.panda_lang.core.interpreter.parser.lexer.Token;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaComposition;
import org.panda_lang.panda.composition.SyntaxComposition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class PandaLexer implements Lexer {

    private final Panda panda;

    public PandaLexer(Panda panda) {
        this.panda = panda;
    }

    @Override
    public Token[] convert(String source) {
        List<Token> tokens = new ArrayList<>();

        PandaComposition pandaComposition = panda.getPandaComposition();
        SyntaxComposition syntaxComposition = pandaComposition.getSyntaxComposition();

        Collection<String> keywords = syntaxComposition.getKeywords();
        Collection<String> separators = syntaxComposition.getSeparators();
        Collection<String> sequences = syntaxComposition.getSequences();

        StringBuilder tokenBuilder = new StringBuilder();
        Stack<String> sequenceStack = new Stack<>();

        char[] sourceCharArray = source.toCharArray();

        for (int i = 0; i < source.length(); i++) {
            char c = sourceCharArray[i];

            if (Character.isWhitespace(c) && sequenceStack.size() == 0) {

            }

            tokenBuilder.append(c);
        }

        return tokens.toArray(new Token[tokens.size()]);
    }

}
