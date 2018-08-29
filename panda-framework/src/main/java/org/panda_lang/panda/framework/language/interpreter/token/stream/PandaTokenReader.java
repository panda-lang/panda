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

package org.panda_lang.panda.framework.language.interpreter.token.stream;

import org.jetbrains.annotations.NotNull;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.utilities.commons.arrays.ArrayDistributor;

public class PandaTokenReader implements TokenReader {

    private final TokenizedSource tokenizedSource;
    private ArrayDistributor<TokenRepresentation> representationsDistributor;
    private PandaTokenReaderIterator iterator;

    public PandaTokenReader(TokenReader tokenReader) {
        this(tokenReader.getTokenizedSource());
        this.setIndex(tokenReader.getIndex());
    }

    public PandaTokenReader(TokenizedSource tokenizedSource) {
        this.tokenizedSource = tokenizedSource;
        this.representationsDistributor = new ArrayDistributor<>(tokenizedSource.toArray());
        this.iterator = new PandaTokenReaderIterator(this);
    }

    @Override
    public TokenRepresentation read() {
        return representationsDistributor.next();
    }

    @NotNull
    @Override
    public PandaTokenReaderIterator iterator() {
        return iterator;
    }

    @Override
    public void setIndex(int index) {
        representationsDistributor.setIndex(index);
        synchronize();
    }

    @Override
    public int getIndex() {
        return representationsDistributor.getIndex();
    }

    @Override
    public TokenizedSource getTokenizedSource() {
        return tokenizedSource;
    }

}
