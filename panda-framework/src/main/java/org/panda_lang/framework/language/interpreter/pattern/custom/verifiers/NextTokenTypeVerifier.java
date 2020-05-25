/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.pattern.custom.verifiers;

import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomVerify;
import org.panda_lang.framework.language.interpreter.token.SynchronizedSource;

import java.util.Map;

public final class NextTokenTypeVerifier implements CustomVerify<Snippetable> {

    private final TokenType type;

    public NextTokenTypeVerifier(TokenType type) {
        this.type = type;
    }

    @Override
    public boolean verify(Map<String, Object> results, SynchronizedSource source, Snippetable content) {
        return source.hasNext() && source.getNext().getType() == type;
    }

}
