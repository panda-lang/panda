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

package org.panda_lang.panda.util.embed;

import org.panda_lang.panda.Panda;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import java.io.Reader;

public class PandaEngine implements ScriptEngine {

    private final Panda panda;
    private final PandaEngineFactory pandaEngineFactory;

    public PandaEngine(Panda panda, PandaEngineFactory pandaEngineFactory) {
        this.panda = panda;
        this.pandaEngineFactory = pandaEngineFactory;
    }

    @Override
    public Object eval(String s, ScriptContext scriptContext) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(Reader reader, ScriptContext scriptContext) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(String s) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(Reader reader) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(String s, Bindings bindings) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(Reader reader, Bindings bindings) throws ScriptException {
        return null;
    }

    @Override
    public void put(String s, Object o) {

    }

    @Override
    public Bindings createBindings() {
        return null;
    }

    @Override
    public void setBindings(Bindings bindings, int i) {

    }

    @Override
    public void setContext(ScriptContext scriptContext) {

    }

    @Override
    public Object get(String s) {
        return null;
    }

    @Override
    public Bindings getBindings(int i) {
        return null;
    }

    @Override
    public ScriptContext getContext() {
        return null;
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return pandaEngineFactory;
    }

    public Panda getPanda() {
        return panda;
    }

}
