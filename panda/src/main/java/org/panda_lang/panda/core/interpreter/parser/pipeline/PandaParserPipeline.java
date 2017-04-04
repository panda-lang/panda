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

package org.panda_lang.panda.core.interpreter.parser.pipeline;

import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.reader.TokenReader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PandaParserPipeline implements ParserPipeline {

    private final List<ParserRepresentation> representations;
    private final Comparator<ParserRepresentation> comparator;
    private int count;

    public PandaParserPipeline() {
        this.representations = new ArrayList<>();
        this.comparator = new ParserRepresentationComparator();
    }

    @Override
    public UnifiedParser handle(SourceStream sourceStream) {
        if (count > 100) {
            count = 0;
            sort();
        }

        for (ParserRepresentation representation : representations) {
            ParserHandler parserHandler = representation.getHandler();
            TokenReader tokenReader = sourceStream.toTokenReader();

            if (parserHandler.handle(tokenReader)) {
                representation.increaseUsages();
                count++;

                return representation.getParser();
            }
        }

        return null;
    }

    public void sort() {
        representations.sort(comparator);
    }

    @Override
    public void registerParserRepresentation(ParserRepresentation parserRepresentation) {
        representations.add(parserRepresentation);
        sort();
    }

    public List<ParserRepresentation> getRepresentations() {
        return representations;
    }

}
