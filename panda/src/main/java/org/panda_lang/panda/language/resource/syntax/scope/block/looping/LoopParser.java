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

package org.panda_lang.panda.language.resource.syntax.scope.block.looping;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.pool.Target;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.parser.PandaParserException;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.interpreter.parser.block.BlockData;
import org.panda_lang.panda.language.interpreter.parser.block.AutowiredBlockParser;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredInitializer;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.autowired.handlers.TokenHandler;
import org.panda_lang.utilities.commons.ArrayUtils;

public final class LoopParser extends AutowiredBlockParser {

    @Override
    public Target<? extends Parser>[] pipeline() {
        return ArrayUtils.of(PandaPipeline.BLOCK);
    }

    @Override
    protected AutowiredInitializer<BlockData> initialize(Context context, AutowiredInitializer<BlockData> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.LOOP))
                .linear("loop value:*=expression");
    }

    @Autowired(order = 1)
    public BlockData parseContent(Context context, @Ctx Scope parent, @Channel Location location, @Src("value") Expression expression) {
        if (!expression.getType().getAssociatedClass().isAssignableTo(Integer.class)) {
            throw new PandaParserException("Loop requires number as an argument");
        }

        return new BlockData(new LoopBlock(parent, location, expression));
    }

}
