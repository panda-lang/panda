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

import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.utilities.commons.iterable.ResourcesIterable;

import java.util.ArrayList;
import java.util.List;

public final class PandaParserPool<P extends Parser> implements ParserPool<P> {

    private final String name;
    private final ParserPool<P> parentPool;
    private final List<P> parsers;

    public PandaParserPool(String name) {
        this(null, name);
    }

    public PandaParserPool(ParserPool<P> parentPool, String name) {
        this.name = name;
        this.parentPool = parentPool;
        this.parsers = new ArrayList<>();
    }

    @Override
    public void register(P parser) {
        parsers.add(parser);
    }

    @Override
    public Iterable<? extends P> getParsers() {
        return parentPool != null ? new ResourcesIterable<>(parentPool.getParsers(), parsers) : parsers;
    }

    @Override
    public String getName() {
        return name;
    }

}
