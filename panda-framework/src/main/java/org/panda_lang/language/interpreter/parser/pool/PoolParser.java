/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.interpreter.parser.pool;

import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.Streamable;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.utilities.commons.function.Option;

import java.util.concurrent.CompletableFuture;

public final class PoolParser<T> implements Parser {

    private final ParserPool<T> pool;

    public PoolParser(ParserPool<T> pool) {
        this.pool = pool;
    }

    /**
     * Parse source using the given pool
     *
     * @param context the context to use
     * @param streamable the source to parse
     * @return true if succeed, otherwise false
     */
    public boolean parse(Context<? extends T> context, Streamable streamable) {
        SourceStream stream = streamable.toStream();

        while (stream.hasUnreadSource()) {
            Context<? extends T> delegatedContext = context.forkCreator()
                    .withSource(stream.toSnippet())
                    .withStream(stream)
                    .toContext();

            if (parseNext(delegatedContext).isEmpty()) {
                throw new PandaParserFailure(delegatedContext, delegatedContext.getSource(), "Unrecognized syntax");
            }
        }

        return true;
    }

    public Option<? extends CompletableFuture<?>> parseNext(Context<? extends T> delegatedContext) {
        SourceStream stream = delegatedContext.getStream();
        int sourceLength = stream.getUnreadLength();

        for (ContextParser<T, ?> parser : pool.getParsers()) {
            Option<? extends CompletableFuture<?>> result = parser.parse(delegatedContext);

            if (result.isEmpty()) {
                stream.unread(sourceLength - stream.getUnreadLength());
                continue;
            }

            if (sourceLength == stream.getUnreadLength()) {
                throw new PandaParserFailure(delegatedContext, delegatedContext.getSource(), parser.getClass().getSimpleName() + " didn't process the source");
            }

            if (stream.hasUnreadSource() && stream.getCurrent().contentEquals(Separators.SEMICOLON)) {
                stream.read();
            }

            return result;
        }

        return Option.none();
    }

    @Override
    public String name() {
        return "pool";
    }

}
