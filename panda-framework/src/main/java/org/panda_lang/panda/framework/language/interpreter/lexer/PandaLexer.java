/*
 * Copyright (c) 2015-2018 Dzikoysk
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
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.resource.Syntax;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenRepresentation;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenizedSource;
import org.panda_lang.panda.framework.language.resource.syntax.auxiliary.Indentation;
import org.panda_lang.panda.utilities.commons.objects.CharacterUtils;
import org.panda_lang.panda.utilities.commons.objects.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

public class PandaLexer implements Lexer {

    private final String title;
    private final String source;
    private final Collection<TokenRepresentation> tokenRepresentations;
    private final Collection<Token> tokenizedLine;
    private final Syntax syntax;

    private final StringBuilder tokenBuilder;
    private final PandaLexerTokenExtractor lexerTokenExtractor;
    private final PandaLexerSequencer lexerSequencer;

    private boolean includeIndentation;
    private boolean respectWhitespaces;

    private String linePreview;
    private String tokenPreview;
    private boolean previousSpecial;
    private int line;

    public PandaLexer(Syntax syntax, Source source) {
        String content = source.getContent();

        if (content == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }
        else if (content.isEmpty()) {
            throw new IllegalArgumentException("Source is empty");
        }

        this.syntax = syntax;
        this.source = content + System.lineSeparator();
        this.title = source.getTitle();

        this.tokenRepresentations = new ArrayList<>();
        this.tokenizedLine = new ArrayList<>();

        this.lexerTokenExtractor = new PandaLexerTokenExtractor(this);
        this.lexerSequencer = new PandaLexerSequencer(this, syntax.getSequences());
        this.tokenBuilder = new StringBuilder();

        this.includeIndentation = false;
        this.respectWhitespaces = true;

        this.tokenPreview = StringUtils.EMPTY;
        this.linePreview = StringUtils.EMPTY;
        this.previousSpecial = false;
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

        if (respectWhitespaces && CharacterUtils.isWhitespace(c)) {
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
        boolean special = CharacterUtils.belongsTo(c, syntax.getSpecialCharacters());

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

        if (includeIndentation) {
            String paragraph = StringUtils.extractParagraph(linePreview);
            Indentation indentation = Indentation.valueOf(paragraph);
            TokenRepresentation representation = new PandaTokenRepresentation(indentation, line);
            tokenRepresentations.add(representation);
        }

        for (Token token : tokenizedLine) {
            TokenRepresentation representation = new PandaTokenRepresentation(token, line);
            tokenRepresentations.add(representation);
        }

        tokenizedLine.clear();
        linePreview = StringUtils.EMPTY;
        line++;
    }

    public void includeIndentation(boolean flag) {
        this.includeIndentation = flag;
    }

    public void respectWhitespaces(boolean flag) {
        this.respectWhitespaces = flag;
    }

    protected int getLine() {
        return line;
    }

    protected StringBuilder getTokenBuilder() {
        return tokenBuilder;
    }

    protected Syntax getSyntax() {
        return syntax;
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

}
