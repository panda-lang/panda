/*
 * Copyright (c) 2015-2016 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.implementation.interpreter.lexer;

import org.panda_lang.framework.interpreter.lexer.Lexer;
import org.panda_lang.framework.interpreter.lexer.TokenRepresentation;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;
import org.panda_lang.framework.interpreter.token.Token;
import org.panda_lang.framework.util.CharacterUtils;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaComposition;
import org.panda_lang.panda.implementation.syntax.SyntaxComposition;

import java.util.ArrayList;
import java.util.Collection;

public class PandaLexer implements Lexer {

    private final Panda panda;
    private final String source;
    private final Collection<TokenRepresentation> tokenRepresentations;
    private final Collection<Token> tokenizedLine;
    private final SyntaxComposition syntaxComposition;

    private final StringBuilder tokenBuilder;
    private final PandaLexerTokenExtractor lexerTokenExtractor;
    private final PandaLexerSequencer lexerSequencer;

    private String linePreview;
    private String tokenPreview;
    private boolean previousSpecial;
    private int line;

    public PandaLexer(Panda panda, String source) {
        if (source == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }
        else if (source.isEmpty()) {
            throw new IllegalArgumentException("Source is empty");
        }

        this.panda = panda;
        this.source = source + System.lineSeparator();
        this.tokenRepresentations = new ArrayList<>();
        this.tokenizedLine = new ArrayList<>();

        PandaComposition pandaComposition = panda.getPandaComposition();
        this.syntaxComposition = pandaComposition.getSyntaxComposition();

        this.lexerTokenExtractor = new PandaLexerTokenExtractor(this);
        this.lexerSequencer = new PandaLexerSequencer(this, syntaxComposition.getSequences());
        this.tokenBuilder = new StringBuilder();

        this.previousSpecial = false;
        this.tokenPreview = "";
        this.linePreview = "";
        this.line = 0;
    }

    @Override
    public TokenizedSource convert() {
        char[] sourceCharArray = source.toCharArray();

        for (char c : sourceCharArray) {
            next(c);
            checkLine();
        }

        TokenRepresentation[] representations = new TokenRepresentation[tokenRepresentations.size()];
        representations = tokenRepresentations.toArray(representations);

        return new PandaTokenizedSource(representations);
    }

    private void next(char c) {
        linePreview += c;
        tokenPreview = tokenBuilder.toString();

        if (lexerSequencer.checkBefore(tokenBuilder, c)) {
            return;
        }

        if (CharacterUtils.isWhitespace(c)) {
            boolean extracted = lexerTokenExtractor.extract(tokenBuilder);

            if (!extracted) {
                throw new PandaLexerException("Unknown token: " + tokenPreview);
            }

            return;
        }

        check(c);
        tokenBuilder.append(c);
        lexerSequencer.checkAfter(tokenBuilder);
    }

    private void check(char c) {
        boolean special = CharacterUtils.belongsTo(c, syntaxComposition.getSpecialCharacters());

        if (previousSpecial && !special) {
            lexerTokenExtractor.extract(tokenBuilder);
        }
        else if (!previousSpecial && special) {
            lexerTokenExtractor.extract(tokenBuilder);
        }

        previousSpecial = special;
    }

    private void checkLine() {
        if (!linePreview.endsWith(System.lineSeparator())) {
            return;
        }

        for (Token token : tokenizedLine) {
            TokenRepresentation representation = new PandaTokenRepresentation(token, line);
            tokenRepresentations.add(representation);
        }

        tokenizedLine.clear();
        linePreview = "";
        line++;
    }

    protected int getLine() {
        return line;
    }

    protected StringBuilder getTokenBuilder() {
        return tokenBuilder;
    }

    protected SyntaxComposition getSyntaxComposition() {
        return syntaxComposition;
    }

    protected Collection<Token> getTokenizedLine() {
        return tokenizedLine;
    }

    protected Collection<TokenRepresentation> getTokenRepresentations() {
        return tokenRepresentations;
    }

    public String getSource() {
        return source;
    }

    public Panda getPanda() {
        return panda;
    }

}
