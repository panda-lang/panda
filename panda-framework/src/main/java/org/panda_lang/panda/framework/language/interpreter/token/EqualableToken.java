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

package org.panda_lang.panda.framework.language.interpreter.token;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;

public abstract class EqualableToken implements Token {

    @Override
    public abstract String getTokenValue();

    @Override
    public abstract TokenType getType();

    @Override
    public String getName() {
        return getType().getTypeName();
    }

    @Override
    public int hashCode() {
        int result = getTokenValue().hashCode();
        result = 31 * result + getType().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof EqualableToken)) {
            return false;
        }

        EqualableToken that = (EqualableToken) o;
        return getType().equals(that.getType()) && getTokenValue().equals(that.getTokenValue());

    }

    @Override
    public String toString() {
        return getType().getTypeName().toLowerCase() + ": " + getTokenValue();
    }

}
