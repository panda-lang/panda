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

import org.panda_lang.panda.framework.design.interpreter.source.Source;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenRepresentation;
import org.panda_lang.panda.framework.language.resource.syntax.auxiliary.Indentation;
import org.panda_lang.panda.utilities.commons.CharacterUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

class PandaLexerWorker {

    private final PandaLexer lexer;
    private final Source source;

    private final Collection<TokenRepresentation> representations = new ArrayList<>();
    private final Collection<Token> tokenizedLine = new ArrayList<>();

    private final StringBuilder tokenBuilder = new StringBuilder();
    private final PandaLexerTokenExtractor extractor = new PandaLexerTokenExtractor(this);
    private final PandaLexerSequencer sequencer;

    private String linePreview = StringUtils.EMPTY;
    private boolean previousSpecial;
    private int line;

    protected PandaLexerWorker(PandaLexer lexer, Source source) {
        this.lexer = lexer;
        this.source = source;
        this.sequencer = new PandaLexerSequencer(this, getConfiguration().syntax.getSequences());
    }

    protected Snippet convert() {
        String content = source.getContent();

        if (content == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }
        else if (content.isEmpty()) {
            throw new IllegalArgumentException("Source is empty");
        }

        char[] sourceCharArray = (content + System.lineSeparator()).toCharArray();

        for (char c : sourceCharArray) {
            next(c);
            checkLine();
        }

        TokenRepresentation[] representations = new TokenRepresentation[this.representations.size()];
        representations = this.representations.toArray(representations);

        return new PandaSnippet(representations);
    }

    private void next(char c) {
        linePreview += c;
        String tokenPreview = tokenBuilder.toString();

        if (sequencer.checkBefore(tokenBuilder, c)) {
            return;
        }

        if (!getConfiguration().ignoringWhitespaces && CharacterUtils.isWhitespace(c)) {
            boolean extracted = extractor.extract(tokenBuilder);

            if (!extracted) {
                throw new PandaLexerException("Unknown token", tokenPreview, linePreview, source.getTitle(), line);
            }

            return;
        }

        check(c);
        tokenBuilder.append(c);
        sequencer.checkAfter(tokenBuilder);
    }

    private void check(char character) {
        boolean special = CharacterUtils.belongsTo(character, getConfiguration().syntax.getSpecialCharacters());

        if (previousSpecial && !special) {
            extractor.extract(tokenBuilder);
        }
        else if (!previousSpecial && special) {
            extractor.extract(tokenBuilder);
        }

        previousSpecial = special;
    }

    private void checkLine() {
        if (!linePreview.endsWith(System.lineSeparator())) {
            return;
        }

        if (getConfiguration().includingIndentation) {
            String paragraph = StringUtils.extractParagraph(linePreview);
            Indentation indentation = Indentation.valueOf(paragraph);
            TokenRepresentation representation = new PandaTokenRepresentation(indentation, line, 0);
            representations.add(representation);
        }

        int position = getConfiguration().includingIndentation ? 1 : 0;

        for (Token token : tokenizedLine) {
            TokenRepresentation representation = new PandaTokenRepresentation(token, line, position++);
            representations.add(representation);
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

    protected PandaLexerConfiguration getConfiguration() {
        return lexer.getConfiguration();
    }

}
