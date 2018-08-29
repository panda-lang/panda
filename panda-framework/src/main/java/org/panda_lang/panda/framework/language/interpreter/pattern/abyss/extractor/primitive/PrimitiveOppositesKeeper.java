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

package org.panda_lang.panda.framework.language.interpreter.pattern.abyss.extractor.primitive;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;

import java.util.Stack;

@Deprecated
class PrimitiveOppositesKeeper {

    private final Stack<Separator> separators;
    private final boolean active;

    protected PrimitiveOppositesKeeper(PrimitiveExtractor tokenExtractor) {
        this.separators = new Stack<>();
        this.active = tokenExtractor.getPattern().hasKeepingOppositesEnabled();
    }

    protected void report(Token token) {
        if (!active) {
            return;
        }

        Separator separator = (token instanceof Separator) ? (Separator) token : null;

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
