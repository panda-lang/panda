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

import org.panda_lang.framework.interpreter.token.TokenInfo;
import org.panda_lang.framework.interpreter.token.PandaSnippet;
import org.panda_lang.framework.interpreter.token.PandaTokenInfo;
import org.panda_lang.framework.resource.syntax.auxiliary.Section;
import org.panda_lang.framework.resource.syntax.separator.Separator;
import panda.std.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

final class PandaLexerCollector {

    private final PandaLexerWorker worker;
    private final List<TokenInfo> representations = new ArrayList<>();
    private final Stack<Pair<TokenInfo, List<TokenInfo>>> sections = new Stack<>();

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

        sections.peek().getSecond().add(representation);
    }

    private boolean processSeparator(TokenInfo separatorRepresentation) {
        Separator separator = separatorRepresentation.toToken();

        if (separator.hasOpposite()) {
            sections.push(new Pair<>(separatorRepresentation, new ArrayList<>()));
            return true;
        }

        if (!sections.isEmpty()) {
            Separator sectionSeparator = sections.peek().getFirst().toToken();

            if (!sectionSeparator.getOpposite().equals(separator)) {
                return false;
            }

            popSection(separatorRepresentation);
            return true;
        }

        return false;
    }

    private void popSection(TokenInfo closingSeparator) {
        Pair<TokenInfo, List<TokenInfo>> sectionData = sections.pop();
        TokenInfo openingSeparator = sectionData.getFirst();

        Section section = new Section(openingSeparator, PandaSnippet.ofImmutable(sectionData.getSecond()), closingSeparator);
        TokenInfo sectionInfo = new PandaTokenInfo(section, openingSeparator.getLocation());

        // add section to root elements
        if (sections.isEmpty()) {
            representations.add(sectionInfo);
            return;
        }

        // add section to parent section
        sections.peek().getSecond().add(sectionInfo);
    }

    protected List<TokenInfo> collect() {
        if (!sections.isEmpty()) {
            throw new PandaLexerFailure(
                    worker.getBuilder().toString(), // current line preview
                    sections.peek().toString(), // indicate current section
                    sections.peek().getFirst().getLocation(), // opening separator points the beginning of section
                    "Cannot find closing separator",
                    ""
            );
        }

        return representations;
    }

}
