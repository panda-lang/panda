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

package org.panda_lang.panda.framework.language.resource.syntax.separator;

import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.EqualableToken;

public class Separator extends EqualableToken {

    private final String separator;
    private Separator opposite;

    public Separator(char separator) {
        this(Character.toString(separator));
    }

    public Separator(String separator) {
        this.separator = separator;
    }

    public void setOpposite(Separator opposite) {
        this.opposite = opposite;
    }

    public boolean hasOpposite() {
        return opposite != null;
    }

    public Separator getOpposite() {
        return opposite;
    }

    @Override
    public String getTokenValue() {
        return separator;
    }

    @Override
    public final TokenType getType() {
        return TokenType.SEPARATOR;
    }

}
