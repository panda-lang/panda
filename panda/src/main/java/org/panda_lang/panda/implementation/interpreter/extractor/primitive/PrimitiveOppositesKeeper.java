/*
 * Copyright (c) 2015-2016 Dzikoysk
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

package org.panda_lang.panda.implementation.interpreter.extractor.primitive;

import org.panda_lang.framework.interpreter.token.Token;
import org.panda_lang.framework.interpreter.token.suggestion.Separator;
import org.panda_lang.panda.implementation.syntax.Separators;

import java.util.Stack;

class PrimitiveOppositesKeeper {

    private final Stack<Separator> separators;
    private final boolean active;

    protected PrimitiveOppositesKeeper(PrimitiveExtractor tokenExtractor) {
        this.separators = new Stack<>();
        this.active = tokenExtractor.getPattern().isKeepingOpposites();
    }

    protected void report(Token token) {
        if (!active) {
            return;
        }

        Separator separator = Separators.valueOf(token);

        if (separator == null) {
            return;
        }

        if (separators.size() > 0) {
            Separator previousSeparator = separators.peek();
            Separator opposite = previousSeparator.getOpposite();

            if (separator.equals(opposite)) {
                separators.pop();
                return;
            }
        }

        if (!separator.hasOpposite()) {
            return;
        }

        separators.push(separator);
    }

    protected boolean isLocked() {
        return separators.size() > 0;
    }

}
