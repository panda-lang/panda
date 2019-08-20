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

package org.panda_lang.panda.framework.language.interpreter.token;

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.utilities.commons.ObjectUtils;
import org.panda_lang.panda.utilities.commons.collection.Lists;
import org.panda_lang.panda.utilities.commons.text.ContentJoiner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PandaSnippet implements Snippet {

    private final List<TokenRepresentation> tokens;

    public PandaSnippet() {
        this.tokens = new ArrayList<>();
    }

    public PandaSnippet(TokenRepresentation... representations) {
        this(Lists.mutableOf(representations));
    }

    public PandaSnippet(List<? extends TokenRepresentation> representations) {
        this(representations, true);
    }

    @SuppressWarnings("unchecked")
    public PandaSnippet(List<? extends TokenRepresentation> representations, boolean clone) {
        this.tokens = clone ? new ArrayList<>(representations) : (List<TokenRepresentation>) representations;
    }

    @Override
    public void addTokens(Snippet snippet) {
        tokens.addAll(snippet.getTokensRepresentations());
    }

    @Override
    public void addToken(TokenRepresentation tokenRepresentation) {
        tokens.add(tokenRepresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokens);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object to) {
        return ObjectUtils.equals(this, tokens, to, tokenRepresentations -> tokenRepresentations.tokens);
    }

    @Override
    public String toString() {
        return ContentJoiner.on(" ")
                .join(tokens, representation -> representation.getToken().toString())
                .toString();
    }

    @Override
    public TokenRepresentation[] toArray() {
        return tokens.toArray(new TokenRepresentation[0]);
    }

    @Override
    public List<? extends TokenRepresentation> getTokensRepresentations() {
        return tokens;
    }

}
