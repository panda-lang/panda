/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.panda.language.interpreter.parser;

import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PipelineParser;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;

public final class ScopeParser implements Parser {

    public Scope parse(Context context, Scope block, Snippet body) throws Exception {
        Context delegatedContext = context.fork()
                .withComponent(Components.STREAM, new PandaSourceStream(body))
                .withComponent(Components.SCOPE, block);

        PipelineParser<?> pipelineParser = new PipelineParser<>(Pipelines.SCOPE, delegatedContext);
        pipelineParser.parse(delegatedContext, false);

        return block;
    }

}
