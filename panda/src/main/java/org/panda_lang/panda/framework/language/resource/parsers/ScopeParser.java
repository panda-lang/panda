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

package org.panda_lang.panda.framework.language.resource.parsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.SnippetUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.linker.PandaScopeLinker;

public class ScopeParser implements Parser {

    private final ContainerParser containerParser = new ContainerParser();

    public void parse(@Nullable Scope parent, Scope current, Context context, @Nullable Snippet body) throws Exception {
        if (SnippetUtils.isEmpty(body)) {
            return;
        }

        ScopeLinker parentLinker = context.getComponent(UniversalComponents.SCOPE_LINKER);
        ScopeLinker linker = parentLinker;

        if (linker == null) {
            linker = new PandaScopeLinker(parent != null ? parent : current);

            if (parent != null) {
                linker.pushScope(current);
            }

            context.withComponent(UniversalComponents.SCOPE_LINKER, linker);
        }
        else {
            linker.pushScope(current);
        }

        containerParser.parse(current, body, context);

        linker.popScope();
        context.withComponent(UniversalComponents.SCOPE_LINKER, parentLinker);
    }

}
