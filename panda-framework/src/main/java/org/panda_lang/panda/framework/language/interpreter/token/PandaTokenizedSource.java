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

package org.panda_lang.panda.framework.language.interpreter.token;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PandaTokenizedSource implements TokenizedSource {

    private final List<TokenRepresentation> tokens;

    public PandaTokenizedSource() {
        this.tokens = new ArrayList<>();
    }

    public PandaTokenizedSource(TokenRepresentation... tokenRepresentations) {
        this(Arrays.asList(tokenRepresentations));
    }

    public PandaTokenizedSource(List<TokenRepresentation> tokenRepresentations) {
        this.tokens = tokenRepresentations;
    }

    @Override
    public TokenizedSource subSource(int fromIndex, int toIndex) {
        return new PandaTokenizedSource(tokens.subList(fromIndex, toIndex));
    }

    @Override
    public TokenRepresentation[] toArray() {
        TokenRepresentation[] array = new TokenRepresentation[tokens.size()];
        return tokens.toArray(array);
    }

    @Override
    public List<TokenRepresentation> getTokensRepresentations() {
        return tokens;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (TokenRepresentation representation : tokens) {
            Token token = representation.getToken();
            builder.append(token.getTokenValue());
            builder.append(" ");
        }

        return builder.toString().trim();
    }

}
