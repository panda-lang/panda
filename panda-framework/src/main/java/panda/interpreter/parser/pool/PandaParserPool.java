/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.parser.pool;

import panda.interpreter.parser.ContextParser;
import panda.utilities.iterable.ResourcesIterable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PandaParserPool<C> implements ParserPool<C> {

    private final String name;
    private final ParserPool<C> parentPool;
    private final List<ContextParser<C, ?>> parsers;

    public PandaParserPool(String name) {
        this(null, name);
    }

    public PandaParserPool(ParserPool<C> parentPool, String name) {
        this.name = name;
        this.parentPool = parentPool;
        this.parsers = new ArrayList<>();
    }

    @Override
    public void register(ContextParser<C, ?> parser) {
        parsers.add(parser);
        Collections.sort(parsers);
    }

    @Override
    public Iterable<ContextParser<C, ?>> getParsers() {
        return parentPool != null ? new ResourcesIterable<>(parentPool.getParsers(), parsers) : parsers;
    }

    @Override
    public String getName() {
        return name;
    }

}
