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

package org.panda_lang.panda.framework.language.resource.syntax.auxiliary;

import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.token.EqualableToken;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;

public class Section extends EqualableToken {

    private final Separator separator;
    private final Snippet content;

    public Section(Separator separator, Snippet content) {
        this.separator = separator;
        this.content = content;
    }

    @Override
    public String getValue() {
        return content.toString();
    }

    @Override
    public TokenType getType() {
        return TokenType.SECTION;
    }

    public Snippet getContent() {
        return content;
    }

    public Separator getSeparator() {
        return separator;
    }

    @Override
    public String toString() {
        return separator.toString() + " " + super.toString() + " " + separator.getOpposite().toString();
    }

}
