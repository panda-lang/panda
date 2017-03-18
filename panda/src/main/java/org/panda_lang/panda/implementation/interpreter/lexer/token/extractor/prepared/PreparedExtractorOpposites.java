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

package org.panda_lang.panda.implementation.interpreter.lexer.token.extractor.prepared;

import org.panda_lang.framework.interpreter.lexer.token.Token;
import org.panda_lang.framework.interpreter.lexer.token.defaults.Separator;
import org.panda_lang.panda.language.syntax.Separators;

import java.util.Stack;

public class PreparedExtractorOpposites {

    private final Stack<Separator> separators;

    public PreparedExtractorOpposites() {
        this.separators = new Stack<>();
    }

    public boolean report(Token token) {
        Separator separator = Separators.valueOf(token);

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

    public int getNestingLevel() {
        return separators.size();
    }

    public boolean isLocked() {
        return separators.size() > 0;
    }

}
