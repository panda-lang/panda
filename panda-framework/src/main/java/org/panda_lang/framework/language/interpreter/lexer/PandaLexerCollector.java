/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.lexer;

import org.panda_lang.framework.design.interpreter.source.Location;
import org.panda_lang.framework.design.interpreter.token.TokenInfo;
import org.panda_lang.framework.language.interpreter.token.MutableTokenInfo;
import org.panda_lang.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.framework.language.interpreter.token.PandaLocation;
import org.panda_lang.framework.language.interpreter.token.PandaTokenInfo;
import org.panda_lang.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.framework.language.resource.syntax.separator.Separator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

final class PandaLexerCollector {

    private final PandaLexerWorker worker;
    private final List<TokenInfo> representations = new ArrayList<>();
    private final Stack<TokenInfo> sections = new Stack<>();
    private final Stack<MutableTokenInfo> closingSeparators = new Stack<>();

    public PandaLexerCollector(PandaLexerWorker worker) {
        this.worker = worker;
    }

    protected void add(TokenInfo representation) {
        if (!worker.getConfiguration().enabledSections) {
            representations.add(representation);
            return;
        }

        if (representation.getToken() instanceof Separator) {
            if (processSeparator(representation)) {
                return;
            }
        }

        if (sections.isEmpty()) {
            representations.add(representation);
            return;
        }

        Section section = sections.peek().toToken();
        section.getContent().addToken(representation);
    }

    private boolean processSeparator(TokenInfo separatorRepresentation) {
        Separator separator = separatorRepresentation.toToken();

        if (separator.hasOpposite()) {
            initializeSection(separatorRepresentation);
            return true;
        }

        if (!sections.isEmpty()) {
            Section currentSection = sections.peek().toToken();

            if (!currentSection.getSeparator().getOpposite().equals(separator)) {
                return false;
            }

            popSection(separatorRepresentation);
            return true;
        }

        return false;
    }

    private void initializeSection(TokenInfo openingSeparator) {
        MutableTokenInfo closingSeparator = new MutableTokenInfo();
        Section section = new Section(openingSeparator, new PandaSnippet(), closingSeparator);

        Location location = openingSeparator.getLocation();
        Location sectionLocation = new PandaLocation(location.getSource(), location.getLine(), location.getIndex() - 1);

        sections.push(new PandaTokenInfo(section, sectionLocation));
        closingSeparators.push(closingSeparator);
    }

    private void popSection(TokenInfo separator) {
        TokenInfo section = sections.pop();

        if (sections.isEmpty()) {
            representations.add(section);
            closingSeparators.pop();
            return;
        }

        Section parentSection = sections.peek().toToken();
        parentSection.getContent().addToken(section);
        closingSeparators.pop().setRepresentation(separator);
    }

    protected List<TokenInfo> collect() {
        if (!sections.isEmpty() || !closingSeparators.isEmpty()) {
            throw new PandaLexerFailure(worker.getBuilder().toString(), sections.peek().toString(), sections.peek().getLocation(), "Cannot find closing separator", "");
        }

        return representations;
    }

}
