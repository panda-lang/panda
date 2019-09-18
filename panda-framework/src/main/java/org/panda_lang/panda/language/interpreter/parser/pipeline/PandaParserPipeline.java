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

package org.panda_lang.panda.language.interpreter.parser.pipeline;

import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.interpreter.InterpreterFailure;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.HandleResult;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class PandaParserPipeline<P extends Parser> implements ParserPipeline<P> {

    private final String name;
    private final ParserPipeline<P> parentPipeline;
    private final List<ParserRepresentation<P>> representations;
    private final Comparator<ParserRepresentation> comparator;
    private long handleTime;
    private int count;

    public PandaParserPipeline(String name) {
        this(null, name);
    }

    public PandaParserPipeline(ParserPipeline<P> parentPipeline, String name) {
        this.name = name;
        this.parentPipeline = parentPipeline;
        this.representations = new ArrayList<>();
        this.comparator = new ParserRepresentationComparator();
    }

    @Override
    public HandleResult<P> handle(Context context, Channel channel, Snippet source) {
        if (count > 1000) {
            count = 0;
            sort();
        }

        if (parentPipeline != null) {
            HandleResult<P> result = handle(context, channel, source, parentPipeline.getRepresentations());

            if (result.isFound()) {
                return result;
            }
        }

        return handle(context, channel, source, representations);
    }

    private HandleResult<P> handle(Context context, Channel channel, Snippet source, Collection<? extends ParserRepresentation<P>> representations) {
        long currentTime = System.nanoTime();
        InterpreterFailure failure = null;

        for (ParserRepresentation<P> representation : representations) {
            ParserHandler handler = representation.getHandler();
            Object value = handler.handle(context, channel, source);

            if (value instanceof InterpreterFailure) {
                failure = (InterpreterFailure) value;
                continue;
            }

            if (value instanceof Exception) {
                Exception exception = (Exception) value;
                throw new PandaFrameworkException("Handler exception: " + exception.getMessage(), exception);
            }

            if (!(value instanceof Boolean)) {
                throw new PandaFrameworkException("Illegal result type from handler " + handler.getClass() + ": returned " + value.getClass());
            }

            if ((Boolean) value) {
                representation.increaseUsages();

                long time = System.nanoTime() - currentTime;
                handleTime += time;
                count++;

                return new PandaHandleResult<>(representation.getParser());
            }
        }

        handleTime += (System.nanoTime() - currentTime);
        return new PandaHandleResult<>(failure);
    }

    protected void sort() {
        representations.sort(comparator);
    }

    @Override
    public void register(ParserRepresentation<P> parserRepresentation) {
        representations.add(parserRepresentation);
        sort();
    }

    @Override
    public long getHandleTime() {
        return handleTime;
    }

    @Override
    public List<? extends ParserRepresentation<P>> getRepresentations() {
        return representations;
    }

    @Override
    public String getName() {
        return name;
    }

}
