/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.resource.syntax.auxiliary;

import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenInfo;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.language.interpreter.token.EqualableToken;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.separator.Separator;

public final class Section extends EqualableToken {

    private final TokenInfo openingSeparator;
    private final TokenInfo closingSeparator;
    private final Snippet content;

    public Section(TokenInfo openingSeparator, Snippet content, TokenInfo closingSeparator) {
        this.openingSeparator = openingSeparator;
        this.closingSeparator = closingSeparator;
        this.content = content;
    }

    @Override
    public String getValue() {
        return content.toString();
    }

    @Override
    public TokenType getType() {
        return TokenTypes.SECTION;
    }

    public Snippet getContent() {
        return content;
    }

    public TokenInfo getOpeningSeparator() {
        return openingSeparator;
    }

    public TokenInfo getClosingSeparator() {
        return closingSeparator;
    }

    public Separator getSeparator() {
        return openingSeparator.toToken();
    }

    @Override
    public String toString() {
        return getSeparator().toString() + " " + super.toString() + " " + getSeparator().getOpposite().toString();
    }

}
