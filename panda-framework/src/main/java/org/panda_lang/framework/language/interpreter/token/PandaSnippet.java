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

package org.panda_lang.framework.language.interpreter.token;

import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenInfo;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.collection.Lists;
import org.panda_lang.utilities.commons.text.ContentJoiner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class PandaSnippet implements Snippet {

    private final List<TokenInfo> tokens;

    public PandaSnippet() {
        this.tokens = new ArrayList<>();
    }

    public PandaSnippet(TokenInfo... representations) {
        this(Lists.mutableOf(representations));
    }

    public PandaSnippet(List<? extends TokenInfo> representations) {
        this(representations, true);
    }

    @SuppressWarnings("unchecked")
    public PandaSnippet(List<? extends TokenInfo> representations, boolean clone) {
        this.tokens = clone ? new ArrayList<>(representations) : (List<TokenInfo>) representations;
    }

    @Override
    public void addTokens(Snippet snippet) {
        tokens.addAll(snippet.getTokensRepresentations());
    }

    @Override
    public void addToken(TokenInfo tokenInfo) {
        tokens.add(tokenInfo);
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
    public TokenInfo[] toArray() {
        return tokens.toArray(new TokenInfo[0]);
    }

    @Override
    public List<? extends TokenInfo> getTokensRepresentations() {
        return tokens;
    }

}
