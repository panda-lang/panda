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

package org.panda_lang.panda.implementation.interpreter.lexer.extractor.prepared;

import org.panda_lang.framework.interpreter.lexer.TokenRepresentation;

public class PreparedRepresentation {

    private final TokenRepresentation tokenRepresentation;
    private final int nestingLevel;

    public PreparedRepresentation(TokenRepresentation tokenRepresentation, int nestingLevel) {
        this.tokenRepresentation = tokenRepresentation;
        this.nestingLevel = nestingLevel;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    public TokenRepresentation getTokenRepresentation() {
        return tokenRepresentation;
    }

}
