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

package org.panda_lang.panda.language.resource.scope;

import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.architecture.statement.VariableData;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.design.interpreter.pattern.custom.Result;
import org.panda_lang.framework.design.interpreter.pattern.custom.elements.KeywordElement;
import org.panda_lang.framework.design.interpreter.pattern.custom.elements.TypeElement;
import org.panda_lang.framework.design.interpreter.pattern.custom.elements.WildcardElement;
import org.panda_lang.framework.design.interpreter.pattern.custom.verifiers.NextTokenTypeVerifier;
import org.panda_lang.framework.design.interpreter.pattern.custom.verifiers.TokenTypeVerifier;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.architecture.statement.VariableDataInitializer;
import org.panda_lang.panda.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.interceptors.CustomPatternInterceptor;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;

@Registrable(pipeline = Pipelines.SCOPE_LABEL, priority = PandaPriorities.CONTAINER_LATE_DECLARATION)
public final class LateDeclarationParser extends ParserBootstrap {

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.LATE))
                .interceptor(new CustomPatternInterceptor())
                .pattern(CustomPattern.of(
                        KeywordElement.create(Keywords.LATE),
                        KeywordElement.create(Keywords.MUT).optional(),
                        KeywordElement.create(Keywords.NIL).optional(),
                        TypeElement.create("type").optional().verify(new NextTokenTypeVerifier(TokenType.UNKNOWN)),
                        WildcardElement.create("name").verify(new TokenTypeVerifier(TokenType.UNKNOWN))
                ));
    }

    @Autowired
    void parse(Context context, @Inter Result result, @Component Scope scope, @Src("type") Snippetable type, @Src("name") Snippetable name) {
        VariableDataInitializer dataInitializer = new VariableDataInitializer(context, scope);
        VariableData variableData = dataInitializer.createVariableData(type, name, result.has(Keywords.MUT.getValue()), result.has(Keywords.NIL.getValue()));
        scope.createVariable(variableData);
    }

}
