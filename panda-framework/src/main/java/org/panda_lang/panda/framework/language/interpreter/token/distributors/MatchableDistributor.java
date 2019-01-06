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

package org.panda_lang.panda.framework.language.interpreter.token.distributors;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;

import java.util.Stack;

public class MatchableDistributor {

    private final TokenDistributor distributor;
    private final Stack<Separator> separators = new Stack<>();
    private int previousSize = 0;

    public MatchableDistributor(TokenDistributor distributor) {
        this.distributor = distributor;
    }

    public TokenRepresentation nextVerified() {
        TokenRepresentation next = next();
        verify();
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

        if (!TokenUtils.isTypeOf(next, TokenType.SEPARATOR)) {
            return;
        }

        Separator separator = (Separator) next.getToken();

        if (separator.hasOpposite()) {
            separators.push(separator);
        }
        else if (!separators.isEmpty() && next.contentEquals(separators.peek().getOpposite())) {
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

    public Tokens subSource(int startIndex, int endIndex) {
        return distributor.subSource(startIndex, endIndex);
    }

    public Tokens currentSubSource() {
        return distributor.currentSubSource();
    }

    public boolean isMatchable() {
        return separators.size() == 0 || previousSize == 0;
    }

    public boolean hasNext() {
        return distributor.hasNext();
    }

    public int getIndex() {
        return distributor.getIndex();
    }

    public TokenDistributor getDistributor() {
        return distributor;
    }

}
