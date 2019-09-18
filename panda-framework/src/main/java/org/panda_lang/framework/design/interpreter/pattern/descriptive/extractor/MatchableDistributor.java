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

package org.panda_lang.framework.design.interpreter.pattern.descriptive.extractor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.pattern.descriptive.utils.TokenDistributor;
import org.panda_lang.framework.language.resource.syntax.separator.Separator;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

final class MatchableDistributor {

    private final TokenDistributor distributor;
    private final Map<Token, Token> replaced = new HashMap<>(0);
    private final Stack<Separator> separators = new Stack<>();
    private int previousSize = 0;

    public MatchableDistributor(TokenDistributor distributor) {
        this.distributor = distributor;
    }

    public TokenRepresentation nextVerified() {
        TokenRepresentation next = next();
        verify(next);
        return next;
    }

    public @Nullable TokenRepresentation verify() {
        TokenRepresentation next = distributor.getNext();
        verify(next);
        return next;
    }

    public void verify(@Nullable TokenRepresentation next) {
        this.previousSize = separators.size();

        if (next == null) {
            return;
        }

        Token token = next.getToken();

        if (replaced.containsKey(token)) {
            token = replaced.get(token);
        }

        if (!(token instanceof Separator)) {
            return;
        }

        Separator separator = (Separator) token;

        if (separator.hasOpposite()) {
            separators.push(separator);
        }
        else if (!separators.isEmpty() && token.equals(separators.peek().getOpposite())) {
            separators.pop();
        }
    }

    public @Nullable void verifyBefore() {
        for (int i = 0; i < distributor.getIndex(); i++) {
            verify(distributor.get(i));
        }
    }

    public TokenRepresentation current() {
        return distributor.current();
    }

    public TokenRepresentation next() {
        return distributor.next();
    }

    public Snippet subSource(int startIndex, int endIndex) {
        return distributor.subSource(startIndex, endIndex);
    }

    public Snippet currentSubSource() {
        return distributor.currentSubSource();
    }

    public MatchableDistributor withReplaced(Map<Token, Token> tokens) {
        this.replaced.putAll(tokens);
        return this;
    }

    public boolean isMatchable() {
        return separators.size() == 0 || previousSize == 0;
    }

    public boolean hasNext() {
        return distributor.hasNext();
    }

    public int getUnreadLength() {
        return distributor.size() - distributor.getIndex();
    }

    public int getIndex() {
        return distributor.getIndex();
    }

    public TokenDistributor getDistributor() {
        return distributor;
    }

}
