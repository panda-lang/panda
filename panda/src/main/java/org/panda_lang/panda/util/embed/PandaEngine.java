/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.util.embed;

import org.panda_lang.panda.Panda;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import java.io.Reader;

@SuppressWarnings("ReturnOfNull")
public final class PandaEngine extends AbstractScriptEngine {

    private final PandaEngineCore core;
    private final PandaEngineFactory factory;

    PandaEngine(PandaEngineFactory factory) {
        this.factory = factory;
        this.core = new PandaEngineCore();
    }

    @Override
    public Object eval(String s, ScriptContext scriptContext) {
        return null;
    }

    @Override
    public Object eval(Reader reader, ScriptContext scriptContext) {
        return null;
    }

    @Override
    public Object eval(String s) {
        return null;
    }

    @Override
    public Object eval(Reader reader) {
        return null;
    }

    @Override
    public Object eval(String s, Bindings bindings) {
        return null;
    }

    @Override
    public Object eval(Reader reader, Bindings bindings) {
        return null;
    }

    @Override
    public Bindings createBindings() {
        return null;
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return factory;
    }

    public Panda getPanda() {
        return core.getPanda();
    }

}
