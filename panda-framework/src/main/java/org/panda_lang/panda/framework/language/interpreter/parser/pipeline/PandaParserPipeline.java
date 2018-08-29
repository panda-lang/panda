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

package org.panda_lang.panda.framework.language.interpreter.parser.pipeline;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class PandaParserPipeline implements ParserPipeline {

    private final ParserPipeline parentPipeline;
    private final List<ParserRepresentation> representations;
    private final Comparator<ParserRepresentation> comparator;
    private long handleTime;
    private int count;

    public PandaParserPipeline() {
        this(null);
    }

    public PandaParserPipeline(ParserPipeline parentPipeline) {
        this.parentPipeline = parentPipeline;
        this.representations = new ArrayList<>();
        this.comparator = new ParserRepresentationComparator();
    }

    @Override
    public UnifiedParser handle(SourceStream stream) {
        if (count > 100) {
            count = 0;
            sort();
        }

        if (parentPipeline != null) {
            UnifiedParser parser = handle(stream, parentPipeline.getRepresentations());

            if (parser != null) {
                return parser;
            }
        }

        return handle(stream, representations);
    }

    private @Nullable UnifiedParser handle(SourceStream stream, Collection<? extends ParserRepresentation> representations) {
        long currentTime = System.nanoTime();

        for (ParserRepresentation representation : representations) {
            ParserHandler parserHandler = representation.getHandler();
            TokenReader tokenReader = stream.toTokenReader();

            if (parserHandler.handle(tokenReader)) {
                representation.increaseUsages();
                count++;

                handleTime += (System.nanoTime() - currentTime);
                return representation.getParser();
            }
        }

        handleTime += (System.nanoTime() - currentTime);
        return null;
    }

    protected void sort() {
        representations.sort(comparator);
    }

    @Override
    public void registerParserRepresentation(ParserRepresentation parserRepresentation) {
        representations.add(parserRepresentation);
        sort();
    }

    @Override
    public long getHandleTime() {
        return handleTime;
    }

    @Override
    public List<? extends ParserRepresentation> getRepresentations() {
        return representations;
    }

}
