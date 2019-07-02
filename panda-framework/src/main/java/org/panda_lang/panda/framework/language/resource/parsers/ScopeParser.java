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
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.SnippetUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.linker.PandaScopeLinker;

import java.util.Optional;

public class ScopeParser implements Parser {

    private final ContainerParser containerParser = new ContainerParser();

    /**
     * Parse the specified source as scope
     *
     * @param context the context to use
     * @param scope the scope to use
     * @param source the source to parse
     * @throws Exception if something happen
     */
    public void parse(Context context, Scope scope, Snippet source) throws Exception {
        parse(context, null, scope, source);
    }

    /**
     * Parse the specified source as scope
     *
     * @param scope the current scope
     * @param parent the parent scope
     * @param context the context to use
     * @param source the source to parse
     * @throws Exception if something happen
     */
    public void parse(Context context, @Nullable Scope parent, Scope scope, Snippet source) throws Exception {
        if (SnippetUtils.isEmpty(source)) {
            return;
        }

        ScopeLinker parentLinker = context.getComponent(UniversalComponents.LINKER);

        if (parentLinker != null) {
            parentLinker.pushScope(scope);
        }

        ScopeLinker linker = Optional.ofNullable(parentLinker).orElseGet(() -> {
            ScopeLinker temp = new PandaScopeLinker(parent != null ? parent : scope);

            if (parent != null) {
                temp.pushScope(scope);
            }

            context.withComponent(UniversalComponents.LINKER, temp);
            return temp;
        });

        containerParser.parse(context, scope, source);

        context.withComponent(UniversalComponents.LINKER, parentLinker);
        linker.popScope();
    }

}
