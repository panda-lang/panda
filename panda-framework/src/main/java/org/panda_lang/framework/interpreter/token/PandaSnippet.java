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

package org.panda_lang.framework.interpreter.token;

import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.text.Joiner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PandaSnippet implements Snippet {

    private static final Snippet EMPTY = new PandaSnippet(Collections.emptyList(), false);

    private final List<TokenInfo> tokens;
    private final boolean mutable;

    @SuppressWarnings("unchecked")
    public PandaSnippet(List<? extends TokenInfo> representations, boolean mutable) {
        this.tokens = mutable ? new ArrayList<>(representations) : (List<TokenInfo>) representations;
        this.mutable = mutable;
    }

    @Override
    public void append(TokenInfo tokenInfo) {
        if (isImmutable()) {
            throw new UnsupportedOperationException("Cannot append token to immutable snippet");
        }

        tokens.add(tokenInfo);
    }

    @Override
    public TokenInfo remove(int index) {
        if (isImmutable()) {
            throw new UnsupportedOperationException("Cannot remove token from immutable snippet");
        }

        return getTokensRepresentations().remove(index);
    }

    @Override
    public int hashCode() {
        return tokens.hashCode();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object to) {
        return ObjectUtils.equals(this, tokens, to, tokenRepresentations -> tokenRepresentations.tokens);
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    @Override
    public String toString() {
        return Joiner.on(" ")
                .join(tokens, Object::toString)
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

    public static Snippet createMutable() {
        return new PandaSnippet(new ArrayList<>(), true);
    }

    public static Snippet ofImmutable(List<? extends TokenInfo> representations) {
        return new PandaSnippet(representations, false);
    }

    public static Snippet ofMutable(List<? extends TokenInfo> representations) {
        return new PandaSnippet(representations, true);
    }

    public static Snippet empty() {
        return EMPTY;
    }

}
