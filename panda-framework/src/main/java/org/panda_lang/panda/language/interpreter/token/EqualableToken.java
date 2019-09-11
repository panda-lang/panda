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

package org.panda_lang.panda.language.interpreter.token;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;

import java.util.Objects;

public abstract class EqualableToken implements Token {

    @Override
    public abstract String getValue();

    @Override
    public abstract TokenType getType();

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getValue(), getName());
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Token && equals((Token) o));
    }

    @Override
    public String toString() {
        return getValue();
    }

}
