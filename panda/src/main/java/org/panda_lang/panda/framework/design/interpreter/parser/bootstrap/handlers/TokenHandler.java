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

package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;

public class TokenHandler implements ParserHandler {

    private final Token[] tokens;

    public TokenHandler(Token... tokens) {
        this.tokens = tokens;
    }

    @Override
    public boolean handle(ParserData data, SourceStream source) {
        Token currentToken = source.toSnippet().getFirst().getToken();

        for (Token token : tokens) {
            if (currentToken.equals(token)) {
                return true;
            }
        }

        return false;
    }

}
