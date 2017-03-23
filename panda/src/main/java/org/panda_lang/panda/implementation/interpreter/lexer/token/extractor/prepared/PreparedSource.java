/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.implementation.interpreter.lexer.token.extractor.prepared;

import org.panda_lang.panda.framework.interpreter.lexer.token.TokenRepresentation;
import org.panda_lang.panda.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.panda.framework.interpreter.lexer.token.Token;

class PreparedSource {

    private final TokenizedSource tokenizedSource;
    private final PreparedRepresentation[] preparedRepresentations;

    protected PreparedSource(TokenizedSource tokenizedSource) {
        this.tokenizedSource = tokenizedSource;
        this.preparedRepresentations = new PreparedRepresentation[tokenizedSource.size()];
        this.prepare();
    }

    private void prepare() {
        PreparedExtractorOpposites opposites = new PreparedExtractorOpposites();

        for (int i = 0; i < tokenizedSource.size(); i++) {
            TokenRepresentation representation = tokenizedSource.get(i);
            Token token = representation.getToken();

            boolean levelUp = opposites.report(token);
            int nestingLevel = levelUp ? opposites.getNestingLevel() - 1 : opposites.getNestingLevel();

            PreparedRepresentation preparedRepresentation = new PreparedRepresentation(representation, nestingLevel);
            preparedRepresentations[i] = preparedRepresentation;
        }
    }

    protected PreparedRepresentation[] getPreparedRepresentations() {
        return preparedRepresentations;
    }

}
