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

package org.panda_lang.panda.framework.language.interpreter.pattern.abyss.extractor;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;

import java.util.Stack;

public class AbyssExtractorOpposites {

    private final Stack<Separator> separators;

    public AbyssExtractorOpposites() {
        this.separators = new Stack<>();
    }

    public boolean report(Token token) {
        Separator separator = (token instanceof Separator) ? (Separator) token : null;

        if (separator == null) {
            return false;
        }

        if (separators.size() > 0) {
            Separator previousSeparator = separators.peek();
            Separator opposite = previousSeparator.getOpposite();

            if (separator.equals(opposite)) {
                separators.pop();
                return false;
            }
        }

        if (!separator.hasOpposite()) {
            return false;
        }

        separators.push(separator);
        return true;
    }

    public boolean isLocked() {
        return separators.size() > 0;
    }

    public int getNestingLevel() {
        return separators.size();
    }

}
