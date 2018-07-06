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

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.utilities.commons.objects.StringUtils;

public class AbyssTokenRepresentation {

    private final TokenRepresentation tokenRepresentation;
    private final int nestingLevel;

    public AbyssTokenRepresentation(TokenRepresentation tokenRepresentation, int nestingLevel) {
        this.tokenRepresentation = tokenRepresentation;
        this.nestingLevel = nestingLevel;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    public TokenRepresentation getTokenRepresentation() {
        return tokenRepresentation;
    }

    @Override
    public String toString() {
        return StringUtils.createIndentation(nestingLevel * 2) + tokenRepresentation + " [" + nestingLevel + "]";
    }

}
