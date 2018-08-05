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

package org.panda_lang.panda.framework.language.interpreter.parser.linker;

import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;

import java.util.Stack;

public class PandaScopeLinker implements ScopeLinker {

    private final Stack<Scope> scopeStack;

    public PandaScopeLinker(Scope parentScope) {
        this.scopeStack = new Stack<>();
        this.pushScope(parentScope);
    }

    @Override
    public void pushScope(Scope scope) {
        scopeStack.push(scope);
    }

    @Override
    public Scope popScope() {
        return scopeStack.pop();
    }

    @Override
    public Scope getCurrentScope() {
        return scopeStack.peek();
    }

    @Override
    public int getNextID() {
        return scopeStack.size();
    }

}
