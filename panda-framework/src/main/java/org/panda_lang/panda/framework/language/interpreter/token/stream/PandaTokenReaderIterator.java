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

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReaderIterator;
import org.panda_lang.panda.utilities.commons.arrays.ArrayDistributor;

public class PandaTokenReaderIterator implements TokenReaderIterator {

    private final TokenReader tokenReader;
    private ArrayDistributor<TokenRepresentation> representationsDistributor;

    public PandaTokenReaderIterator(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
        this.representationsDistributor = new ArrayDistributor<>(tokenReader.getTokenizedSource().toArray());
    }

    @Override
    public void synchronize() {
        setIndex(tokenReader.getIndex());
    }

    @Override
    public TokenRepresentation next() {
        return representationsDistributor.next();
    }

    @Override
    public TokenRepresentation previous() {
        return representationsDistributor.previous();
    }

    @Override
    public void setIndex(int index) {
        representationsDistributor.setIndex(index);
    }

    @Override
    public boolean hasNext() {
        return getIndex() + 1 < tokenReader.getTokenizedSource().size();
    }

    public ArrayDistributor<TokenRepresentation> getRepresentationsDistributor() {
        return representationsDistributor;
    }

    @Override
    public int getIndex() {
        return representationsDistributor.getIndex();
    }

}
