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

package org.panda_lang.panda.framework.language.interpreter.token.distributor;

import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.reader.PandaTokenReader;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.reader.TokenReader;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PandaSourceStream implements SourceStream {

    private TokenizedSource source;
    private TokenizedSource cachedSource;

    public PandaSourceStream(TokenizedSource source) {
        this.source = source;
        this.cachedSource = source;
    }

    @Override
    public TokenRepresentation read() {
        if (source.size() < 1) {
            throw new NoSuchElementException("SourceStream is empty, cannot read next TokenRepresentation");
        }

        TokenRepresentation representation = source.get(0);
        List<TokenRepresentation> tokens = source.getTokensRepresentations().subList(1, source.size());

        this.cachedSource = this.source;
        this.source = new PandaTokenizedSource(tokens);

        return representation;
    }

    @Override
    public TokenRepresentation[] read(int length) {
        TokenRepresentation[] array = new TokenRepresentation[length];
        List<TokenRepresentation> tokens = source.getTokensRepresentations();

        for (int i = 0; i < array.length && i < tokens.size(); i++) {
            TokenRepresentation representation = tokens.get(i);
            array[i] = representation;
        }

        tokens = length < tokens.size() ? tokens.subList(length, tokens.size()) : new ArrayList<>();

        this.cachedSource = this.source;
        this.source = new PandaTokenizedSource(tokens);

        return array;
    }

    @Override
    public TokenRepresentation[] readDifference(TokenReader reader) {
        int length = reader.getIndex() + 1;
        return this.read(length);
    }

    @Override
    public void restoreCachedSource() {
        this.source = this.cachedSource;
    }

    @Override
    public TokenReader toTokenReader() {
        return new PandaTokenReader(source);
    }

    @Override
    public TokenizedSource toTokenizedSource() {
        return this.source;
    }

    @Override
    public String toString() {
        return "PandaSourceStream['" + this.source.toString() + "']";
    }

}
