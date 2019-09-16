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

package org.panda_lang.panda.language.resource.parsers;

import org.panda_lang.panda.framework.design.architecture.dynamic.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.language.interpreter.token.PandaSourceStream;

public class ScopeParser implements Parser {

    public Scope parse(Context context, Scope scope, Snippet body) throws Exception {
        Scope previous = context.getComponent(UniversalComponents.SCOPE);

        Context delegatedContext = context.fork()
                .withComponent(UniversalComponents.STREAM, new PandaSourceStream(body))
                .withComponent(UniversalComponents.SCOPE, scope);

        PipelineParser<?> pipelineParser = new PipelineParser<>(UniversalPipelines.SCOPE, delegatedContext);
        pipelineParser.parse(delegatedContext, false);

        delegatedContext.withComponent(UniversalComponents.SCOPE, previous);
        return scope;
    }

}
