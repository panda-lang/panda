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

package org.panda_lang.panda.language.syntax.scope.block.conditional;

import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.syntax.scope.block.BlockParser;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

public final class ElseIfParser extends BlockParser<ConditionalBlock> {

    private static final ConditionalParser CONDITIONAL_PARSER = new ConditionalParser();

    @Override
    public String name() {
        return "else if";
    }

    @Override
    public Option<Completable<ConditionalBlock>> parse(Context<?> context) {
        return CONDITIONAL_PARSER.parse(SCOPE_PARSER, context, true, false, Keywords.ELSE, Keywords.IF)
                .peek(conditionalBlock -> CONDITIONAL_PARSER.linkBlocks(context, conditionalBlock))
                .map(Completable::completed);
    }

}
