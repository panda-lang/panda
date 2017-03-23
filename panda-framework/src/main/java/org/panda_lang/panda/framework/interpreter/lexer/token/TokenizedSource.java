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

package org.panda_lang.panda.framework.interpreter.lexer.token;

import java.util.List;

public interface TokenizedSource {

    default int size() {
        return getTokensRepresentations().size();
    }

    default void addToken(TokenRepresentation tokenRepresentation) {
        getTokensRepresentations().add(tokenRepresentation);
    }

    default TokenRepresentation get(int id) {
        if (id >= size() || id < 0) {
            return null;
        }

        return getTokensRepresentations().get(id);
    }

    default Token getToken(int id) {
        TokenRepresentation tokenRepresentation = get(id);

        if (tokenRepresentation == null) {
            return null;
        }

        return tokenRepresentation.getToken();
    }

    TokenRepresentation[] toArray();

    List<TokenRepresentation> getTokensRepresentations();

    static String asString(TokenizedSource tokenizedSource) {
        StringBuilder node = new StringBuilder();

        for (TokenRepresentation representation : tokenizedSource.getTokensRepresentations()) {
            Token token = representation.getToken();
            node.append(token.getTokenValue());
        }

        return node.toString();
    }

}
