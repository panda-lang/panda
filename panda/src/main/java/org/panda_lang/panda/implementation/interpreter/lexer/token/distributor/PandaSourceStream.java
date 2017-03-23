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

package org.panda_lang.panda.implementation.interpreter.lexer.token.distributor;

import org.panda_lang.panda.framework.interpreter.lexer.token.TokenRepresentation;
import org.panda_lang.panda.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.panda.framework.interpreter.lexer.token.distributor.SourceStream;
import org.panda_lang.panda.framework.interpreter.lexer.token.reader.TokenReader;
import org.panda_lang.panda.implementation.interpreter.lexer.token.PandaTokenizedSource;
import org.panda_lang.panda.implementation.interpreter.lexer.token.reader.PandaTokenReader;

import java.util.ArrayList;
import java.util.List;

public class PandaSourceStream implements SourceStream {

    private TokenizedSource tokenizedSource;

    public PandaSourceStream(TokenizedSource tokenizedSource) {
        this.tokenizedSource = tokenizedSource;
    }

    public TokenRepresentation read() {
        if (tokenizedSource.size() < 1) {
            return null;
        }

        TokenRepresentation representation = tokenizedSource.get(0);
        List<TokenRepresentation> tokens = tokenizedSource.getTokensRepresentations().subList(1, tokenizedSource.size());

        tokenizedSource = new PandaTokenizedSource(tokens);
        return representation;
    }

    public TokenRepresentation[] read(int length) {
        TokenRepresentation[] array = new TokenRepresentation[length];
        List<TokenRepresentation> tokens = tokenizedSource.getTokensRepresentations();

        for (int i = 0; i < array.length && i < tokens.size(); i++) {
            TokenRepresentation representation = tokens.get(i);
            array[i] = representation;
        }

        tokens = length < tokens.size() ? tokens.subList(length, tokens.size()) : new ArrayList<>();
        this.tokenizedSource = new PandaTokenizedSource(tokens);

        return array;
    }

    @Override
    public TokenRepresentation[] readDifference(TokenReader reader) {
        int length = reader.getIndex() + 1;
        return this.read(length);
    }

    @Override
    public boolean hasUnreadSource() {
        return tokenizedSource.size() > 0;
    }

    @Override
    public TokenReader toTokenReader() {
        return new PandaTokenReader(tokenizedSource);
    }

    @Override
    public TokenizedSource toTokenizedSource() {
        return this.tokenizedSource;
    }

}
