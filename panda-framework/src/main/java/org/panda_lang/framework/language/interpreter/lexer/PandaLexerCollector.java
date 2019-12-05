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

package org.panda_lang.framework.language.interpreter.lexer;

import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.interpreter.token.MutableTokenRepresentation;
import org.panda_lang.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.framework.language.interpreter.token.PandaSourceLocation;
import org.panda_lang.framework.language.interpreter.token.PandaTokenRepresentation;
import org.panda_lang.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.framework.language.resource.syntax.separator.Separator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

final class PandaLexerCollector {

    private final PandaLexerWorker worker;
    private final List<TokenRepresentation> representations = new ArrayList<>();
    private final Stack<TokenRepresentation> sections = new Stack<>();
    private final Stack<MutableTokenRepresentation> closingSeparators = new Stack<>();

    public PandaLexerCollector(PandaLexerWorker worker) {
        this.worker = worker;
    }

    protected void add(TokenRepresentation representation) {
        if (!worker.getConfiguration().enabledSections) {
            representations.add(representation);
            return;
        }

        // System.out.println("[LEXER] " + StringUtils.buildSpace(sections.size() * 2) + representation.toString());

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

    private boolean processSeparator(TokenRepresentation separatorRepresentation) {
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

    private void initializeSection(TokenRepresentation openingSeparator) {
        MutableTokenRepresentation closingSeparator = new MutableTokenRepresentation();
        Section section = new Section(openingSeparator, new PandaSnippet(), closingSeparator);

        SourceLocation location = openingSeparator.getLocation();
        SourceLocation sectionLocation = new PandaSourceLocation(location.getSource(), location.getLine(), location.getIndex() - 1);

        sections.push(new PandaTokenRepresentation(section, sectionLocation));
        closingSeparators.push(closingSeparator);
    }

    private void popSection(TokenRepresentation separator) {
        TokenRepresentation section = sections.pop();

        if (sections.isEmpty()) {
            representations.add(section);
            closingSeparators.pop();
            return;
        }

        Section parentSection = sections.peek().toToken();
        parentSection.getContent().addToken(section);
        closingSeparators.pop().setRepresentation(separator);
    }

    protected List<TokenRepresentation> collect() {
        if (!sections.isEmpty() || !closingSeparators.isEmpty()) {
            throw new PandaLexerFailure(worker.getBuilder().toString(), sections.peek().toString(), sections.peek().getLocation(), "Cannot find closing separator", "");
        }

        return representations;
    }

}
