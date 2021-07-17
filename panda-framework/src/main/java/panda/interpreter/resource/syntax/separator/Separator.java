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

package panda.interpreter.resource.syntax.separator;

import panda.interpreter.token.TokenType;
import panda.interpreter.token.EqualableToken;
import panda.interpreter.resource.syntax.TokenTypes;

public final class Separator extends EqualableToken {

    private final String separator;
    private final Separator opposite;

    public Separator(char separator, Separator opposite) {
        this(Character.toString(separator), opposite);
    }

    public Separator(String separator, Separator opposite) {
        this.separator = separator;
        this.opposite = opposite;
    }

    public boolean hasOpposite() {
        return opposite != null;
    }

    public Separator getOpposite() {
        return opposite;
    }

    @Override
    public String getValue() {
        return separator;
    }

    @Override
    public final TokenType getType() {
        return TokenTypes.SEPARATOR;
    }

}
