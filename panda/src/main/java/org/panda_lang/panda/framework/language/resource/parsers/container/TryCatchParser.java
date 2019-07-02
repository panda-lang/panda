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

package org.panda_lang.panda.framework.language.resource.parsers.container;

import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.architecture.statement.PandaContainer;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.language.architecture.dynamic.TryCatchExecutable;
import org.panda_lang.panda.framework.language.resource.parsers.ContainerParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.subparsers.variable.VariableParser;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(pipeline = UniversalPipelines.CONTAINER_LABEL)
public final class TryCatchParser extends ParserBootstrap {

    private final ContainerParser containerParser = new ContainerParser();
    private final VariableParser initializer = new VariableParser();

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.TRY))
                .pattern("try try-body:~{ catch catch-what:~( catch-body:~{");
    }

    @Autowired
    void parse(Context context, @Component Container container, @Src("try-body") Snippet tryBody, @Src("catch-what") Snippet catchWhat, @Src("catch-body") Snippet catchBody) throws Exception {
        Container tryContainer = containerParser.parse(context, new PandaContainer(), tryBody);

        Scope scope = context.getComponent(UniversalComponents.LINKER).getCurrentScope();
        Variable variable = initializer.parseVariable(context, scope, true, true, catchWhat);
        int variablePointer = scope.indexOf(variable);

        TryCatchExecutable tryCatch = new TryCatchExecutable(tryContainer, new PandaContainer());
        container.addStatement(tryCatch);

        Class<?> type = variable.getType().getAssociatedClass();

        if (Throwable.class.isAssignableFrom(type)) {
            //noinspection unchecked
            tryCatch.addHandler((Class<? extends Throwable>) type, variable, variablePointer, containerParser.parse(context, new PandaContainer(), catchBody));
        }
    }

}
