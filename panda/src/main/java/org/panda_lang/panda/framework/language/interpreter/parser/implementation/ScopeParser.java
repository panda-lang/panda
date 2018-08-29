/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation;

import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.linker.PandaScopeLinker;

public class ScopeParser implements Parser {

    private final Scope scope;
    private ParserData data;

    private ScopeParser(Scope scope, ParserData data) {
        this.scope = scope;
        this.data = data;
    }

    public ScopeParser forkData() {
        this.data = data.fork();
        return this;
    }

    public ScopeParser initializeLinker() {
        data.setComponent(PandaComponents.SCOPE_LINKER, new PandaScopeLinker(scope));
        return this;
    }

    public ScopeParser initializeLinker(Scope parentScope, Scope currentScope) {
        ScopeLinker linker = new PandaScopeLinker(parentScope);
        linker.pushScope(currentScope);
        data.setComponent(PandaComponents.SCOPE_LINKER, linker);
        return this;
    }

    public void parse(TokenizedSource body) {
        ContainerParser parser = new ContainerParser(scope);
        parser.parse(data, body);
    }

    public static ScopeParser createParser(Scope parent, ParserData data) {
        return new ScopeParser(parent, data);
    }

}
