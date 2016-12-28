/*
 * Copyright (c) 2015-2016 Dzikoysk
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

package org.panda_lang.panda.util.spcl.storage.util;

import org.panda_lang.framework.interpreter.extractor.Extractor;
import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.lexer.TokenRepresentation;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;
import org.panda_lang.framework.interpreter.token.Token;
import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.panda.implementation.interpreter.extractor.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenReader;
import org.panda_lang.panda.util.spcl.value.SPCLSection;

import java.util.List;

public class SPCLParser {

    private final SPCLSection section;
    private final SPCLSyntax syntax;

    public SPCLParser(SPCLSection section) {
        this.section = section;
        this.syntax = new SPCLSyntax();
    }

    public void parse(String source) {
        PandaLexer lexer = new PandaLexer(syntax, source);
        lexer.includeIndentation();

        TokenizedSource tokenizedSource = lexer.convert();
        TokenReader reader = new PandaTokenReader(tokenizedSource);

        TokenPattern pattern = TokenPattern.builder()
                .gap()
                .unit(TokenType.SEPARATOR, System.lineSeparator())
                .build();

        Extractor extractor = pattern.extractor();
        List<TokenizedSource> tokenizedLines = extractor.extract(reader);

        parse(tokenizedLines);
    }

    public void parse(List<TokenizedSource> tokenizedLines) {
        SPCLSection currentSection = section;
        SPCLSection previousSection = null;
        int previousIndentation = 0;

        for (TokenizedSource tokenizedLine : tokenizedLines) {
            for (TokenRepresentation representation : tokenizedLine.getTokensRepresentations()) {
                Token token = representation.getToken();

            }
        }
    }

}
