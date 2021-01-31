/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.interpreter.lexer;

import org.panda_lang.framework.interpreter.source.Source;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.interpreter.token.Token;
import org.panda_lang.framework.interpreter.token.TokenInfo;
import org.panda_lang.framework.interpreter.token.PandaSnippet;
import org.panda_lang.framework.interpreter.token.PandaLocation;
import org.panda_lang.framework.interpreter.token.PandaTokenInfo;
import org.panda_lang.framework.resource.syntax.auxiliary.Indentation;
import org.panda_lang.utilities.commons.CharacterUtils;
import org.panda_lang.utilities.commons.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

final class PandaLexerWorker {

    private final PandaLexer lexer;
    private final Source source;

    private final StringBuilder builder = new StringBuilder();
    private final Collection<Token> lineTokens = new ArrayList<>();
    private final PandaLexerTokenExtractor extractor = new PandaLexerTokenExtractor(this);
    private final PandaLexerSequencer sequencer = new PandaLexerSequencer(this);
    private final PandaLexerCollector collector = new PandaLexerCollector(this);

    private final StringBuilder linePreview = new StringBuilder();
    private boolean previousSpecial;
    private int line;

    protected PandaLexerWorker(PandaLexer lexer, Source source) {
        this.lexer = lexer;
        this.source = source;
    }

    protected Snippet convert() {
        String content = source.getContent();

        if (content == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }
        else if (content.isEmpty()) {
            throw new IllegalArgumentException("Source is empty");
        }

        content = StringUtils.replace(content, System.lineSeparator(), "\n");
        char[] sourceArray = (content + "\n").toCharArray();

        for (char character : sourceArray) {
            next(character);
            checkLine();
        }

        return PandaSnippet.ofImmutable(collector.collect());
    }

    private void next(char character) {
        linePreview.append(character);
        String tokenPreview = builder.toString();

        if (sequencer.checkBefore(builder, character)) {
            return;
        }

        if (!getConfiguration().ignoringWhitespaces && CharacterUtils.isWhitespace(character) && !sequencer.isOpened()) {
            boolean extracted = extractor.extract(builder);

            if (extracted) {
                return;
            }

            throw new PandaLexerFailure(linePreview, tokenPreview, new PandaLocation(source, line, lineTokens.size()), "Cannot recognize token", null);
        }

        check(character);
        builder.append(character);
        sequencer.checkAfter(builder);
    }

    private void check(char character) {
        boolean special = CharacterUtils.belongsTo(character, getConfiguration().syntax.getSpecialCharacters());

        if (previousSpecial && !special) {
            extractor.extract(builder);
        }
        else if (!previousSpecial && special) {
            extractor.extract(builder);
        }

        previousSpecial = special;
    }

    private void checkLine() {
        if (!linePreview.toString().endsWith("\n")) {
            return;
        }

        if (getConfiguration().includingIndentation) {
            String paragraph = StringUtils.extractParagraph(linePreview.toString());
            Indentation indentation = Indentation.valueOf(paragraph);
            TokenInfo representation = new PandaTokenInfo(indentation, new PandaLocation(source, line, 0));
            collector.add(representation);
        }

        int position = getConfiguration().includingIndentation ? 1 : 0;

        for (Token token : lineTokens) {
            TokenInfo representation = new PandaTokenInfo(token, new PandaLocation(source, line, position++));
            collector.add(representation);
        }

        lineTokens.clear();
        linePreview.setLength(0);
        line++;
    }

    protected void addLineToken(Token token) {
        lineTokens.add(token);
    }

    protected StringBuilder getBuilder() {
        return builder;
    }

    protected PandaLexerConfiguration getConfiguration() {
        return lexer.getConfiguration();
    }

}
