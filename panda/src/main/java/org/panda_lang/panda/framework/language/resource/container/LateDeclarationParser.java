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

package org.panda_lang.panda.framework.language.resource.container;

import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Inter;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.parser.loader.Registrable;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.resource.container.assignation.variable.VariableParser;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@Registrable(pipeline = UniversalPipelines.CONTAINER_LABEL, priority = PandaPriorities.CONTAINER_LATE_DECLARATION)
public class LateDeclarationParser extends ParserBootstrap {

    private static final VariableParser INITIALIZER = new VariableParser();

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.LATE))
                .pattern("late " + VariableParser.DECLARATION);
    }

    @Autowired
    void parse(Context context, @Inter ExtractorResult result, @Component ScopeLinker linker, @Src("type") Snippet type, @Src("name") Snippet name) {
        boolean mutable = result.hasIdentifier(Keywords.MUT.getValue());
        boolean nillable = result.hasIdentifier(Keywords.NIL.getValue());

        INITIALIZER.createVariable(context, linker.getCurrentScope(), mutable, nillable, type, name);
    }

}
