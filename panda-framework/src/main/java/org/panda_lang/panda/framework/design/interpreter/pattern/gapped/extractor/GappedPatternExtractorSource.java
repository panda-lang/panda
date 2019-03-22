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

package org.panda_lang.panda.framework.design.interpreter.pattern.gapped.extractor;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

class GappedPatternExtractorSource {

    private final Snippet snippet;
    private final GappedTokenRepresentation[] abyssRepresentations;

    protected GappedPatternExtractorSource(Snippet snippet) {
        this.snippet = snippet;
        this.abyssRepresentations = new GappedTokenRepresentation[snippet.size()];
        this.prepare();
    }

    private void prepare() {
        GappedPatternExtractorOpposites opposites = new GappedPatternExtractorOpposites();

        for (int i = 0; i < snippet.size(); i++) {
            TokenRepresentation representation = snippet.get(i);

            if (representation == null) {
                throw new PandaRuntimeException("Representation is null");
            }

            Token token = representation.getToken();

            boolean levelUp = opposites.report(token);
            int nestingLevel = levelUp ? opposites.getNestingLevel() - 1 : opposites.getNestingLevel();

            GappedTokenRepresentation abyssRepresentation = new GappedTokenRepresentation(representation, nestingLevel);
            abyssRepresentations[i] = abyssRepresentation;
        }
    }

    protected GappedTokenRepresentation[] getAbyssRepresentations() {
        return abyssRepresentations;
    }

}
