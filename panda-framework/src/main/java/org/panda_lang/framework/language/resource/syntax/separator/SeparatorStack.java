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

package org.panda_lang.framework.language.resource.syntax.separator;

import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.utilities.commons.ObjectUtils;

import java.util.Stack;

public class SeparatorStack {

    private final Stack<Separator> separators = new Stack<>();

    /**
     * Check a next token
     *
     * @param token the token to check
     * @return true if token affected the stack, otherwise false
     */
    public boolean check(Token token) {
        Separator separator = ObjectUtils.cast(Separator.class, token);

        if (separator == null) {
            return false;
        }

        if (separator.hasOpposite()) {
            separators.push(separator);
            return true;
        }

        if (!isLocked()) {
            return false;
        }

        if (!separators.peek().getOpposite().equals(separator)) {
            return false;
        }

        separators.pop();
        return true;
    }

    /**
     * Check if the stack contains some separators
     *
     * @return true if some stack contains some separators
     */
    public boolean isLocked() {
        return !separators.isEmpty();
    }

}
