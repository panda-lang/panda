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

package org.panda_lang.panda.framework.language.interpreter.pattern.abyss.extractor;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;

class AbyssExtractorSource {

    private final TokenizedSource tokenizedSource;
    private final AbyssTokenRepresentation[] abyssRepresentations;

    protected AbyssExtractorSource(TokenizedSource tokenizedSource) {
        this.tokenizedSource = tokenizedSource;
        this.abyssRepresentations = new AbyssTokenRepresentation[tokenizedSource.size()];
        this.prepare();
    }

    private void prepare() {
        AbyssExtractorOpposites opposites = new AbyssExtractorOpposites();

        for (int i = 0; i < tokenizedSource.size(); i++) {
            TokenRepresentation representation = tokenizedSource.get(i);
            Token token = representation.getToken();

            boolean levelUp = opposites.report(token);
            int nestingLevel = levelUp ? opposites.getNestingLevel() - 1 : opposites.getNestingLevel();

            AbyssTokenRepresentation abyssRepresentation = new AbyssTokenRepresentation(representation, nestingLevel);
            abyssRepresentations[i] = abyssRepresentation;
        }
    }

    protected AbyssTokenRepresentation[] getAbyssRepresentations() {
        return abyssRepresentations;
    }

}
