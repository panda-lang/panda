/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.lexer;

import org.panda_lang.panda.framework.design.interpreter.lexer.Lexer;
import org.panda_lang.panda.framework.design.interpreter.source.Source;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.Syntax;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenRepresentation;
import org.panda_lang.panda.framework.language.resource.syntax.auxiliary.Indentation;
import org.panda_lang.panda.utilities.commons.CharacterUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

public class PandaLexer implements Lexer {

    private final PandaLexerConfiguration configuration;

    private final String title;
    private final String source;
    private final Collection<TokenRepresentation> tokenRepresentations;
    private final Collection<Token> tokenizedLine;

    private final StringBuilder tokenBuilder;
    private final PandaLexerTokenExtractor lexerTokenExtractor;
    private final PandaLexerSequencer lexerSequencer;
    private String linePreview;
    private String tokenPreview;
    private boolean previousSpecial;
    private int line;

    protected PandaLexer(PandaLexerConfiguration configuration) {
        this.configuration = configuration;

        Source source = configuration.getSource();
        String content = source.getContent();

        if (content == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }
        else if (content.isEmpty()) {
            throw new IllegalArgumentException("Source is empty");
        }

        this.source = content + System.lineSeparator();
        this.title = source.getTitle();

        this.tokenRepresentations = new ArrayList<>();
        this.tokenizedLine = new ArrayList<>();

        this.lexerTokenExtractor = new PandaLexerTokenExtractor(this);
        this.lexerSequencer = new PandaLexerSequencer(this, configuration.getSyntax().getSequences());
        this.tokenBuilder = new StringBuilder();

        this.tokenPreview = StringUtils.EMPTY;
        this.linePreview = StringUtils.EMPTY;
        this.previousSpecial = false;
        this.line = 0;
    }

    @Override
    public Snippet convert() {
        char[] sourceCharArray = source.toCharArray();

        for (char c : sourceCharArray) {
            next(c);
            checkLine();
        }

        TokenRepresentation[] representations = new TokenRepresentation[tokenRepresentations.size()];
        representations = tokenRepresentations.toArray(representations);

        return new PandaSnippet(representations);
    }

    private void next(char c) {
        linePreview += c;
        tokenPreview = tokenBuilder.toString();

        if (lexerSequencer.checkBefore(tokenBuilder, c)) {
            return;
        }

        if (configuration.isRespectingWhitespaces() && CharacterUtils.isWhitespace(c)) {
            boolean extracted = lexerTokenExtractor.extract(tokenBuilder);

            if (!extracted) {
                throw new PandaLexerException("Unknown token", tokenPreview, linePreview, title, line);
            }

            return;
        }

        check(c);
        tokenBuilder.append(c);
        lexerSequencer.checkAfter(tokenBuilder);
    }

    private void check(char c) {
        boolean special = CharacterUtils.belongsTo(c, configuration.getSyntax().getSpecialCharacters());

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

        if (configuration.hasIncludedIndentation()) {
            String paragraph = StringUtils.extractParagraph(linePreview);
            Indentation indentation = Indentation.valueOf(paragraph);
            TokenRepresentation representation = new PandaTokenRepresentation(indentation, line, 0);
            tokenRepresentations.add(representation);
        }

        int position = configuration.hasIncludedIndentation() ? 1 : 0;

        for (Token token : tokenizedLine) {
            TokenRepresentation representation = new PandaTokenRepresentation(token, line, position++);
            tokenRepresentations.add(representation);
        }

        tokenizedLine.clear();
        linePreview = StringUtils.EMPTY;
        line++;
    }


    protected int getLine() {
        return line;
    }

    protected StringBuilder getTokenBuilder() {
        return tokenBuilder;
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

    public PandaLexerConfiguration getConfiguration() {
        return configuration;
    }

    public static PandaLexerConfiguration of(Syntax syntax, Source source) {
        return new PandaLexerConfiguration(syntax, source);
    }

    public static PandaLexerConfiguration builder() {
        return new PandaLexerConfiguration();
    }

}
