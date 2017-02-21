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

package org.panda_lang.panda.language.structure.prototype.method.invoker;

import org.panda_lang.framework.interpreter.lexer.token.extractor.Extractor;
import org.panda_lang.framework.interpreter.lexer.token.reader.TokenReader;
import org.panda_lang.framework.interpreter.parser.ParserHandler;
import org.panda_lang.panda.implementation.interpreter.lexer.token.reader.PandaTokenReader;

public class MethodInvokerParserHandler implements ParserHandler {

    @Override
    public boolean handle(TokenReader tokenReader) {
        TokenReader copyOfTokenReader = new PandaTokenReader(tokenReader);
        Extractor extractor = MethodInvokerParser.PATTERN.extractor();

        return extractor.extract(copyOfTokenReader) != null;
    }

}
