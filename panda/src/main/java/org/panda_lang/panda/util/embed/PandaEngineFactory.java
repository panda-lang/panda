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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.Panda;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.List;

public class PandaEngineFactory implements ScriptEngineFactory {

    private final PandaEngine pandaEngine;

    public PandaEngineFactory(Panda panda) {
        this.pandaEngine = new PandaEngine(panda, this);
    }

    @Override
    public @Nullable Object getParameter(String s) {
        return null; // TODO
    }

    @Override
    public String getMethodCallSyntax(String instance, String method, String... parameters) {
        StringBuilder methodCallBuilder = new StringBuilder();
        methodCallBuilder.append(instance);
        methodCallBuilder.append(".");
        methodCallBuilder.append(method);
        methodCallBuilder.append("(");

        for (int i = 0; i < parameters.length; i++) {
            methodCallBuilder.append(parameters[i]);

            if (i + 1 < parameters.length) {
                methodCallBuilder.append(",");
            }
        }

        methodCallBuilder.append(")");
        return methodCallBuilder.toString();
    }

    @Override
    public @Nullable String getOutputStatement(String s) {
        return null; // TODO
    }

    @Override
    public @Nullable String getProgram(String... strings) {
        return null; // TODO
    }

    @Override
    public String getEngineName() {
        return PandaEngineFactoryConstants.ENGINE_NAME;
    }

    @Override
    public String getEngineVersion() {
        return PandaEngineFactoryConstants.ENGINE_VERSION;
    }

    @Override
    public List<String> getExtensions() {
        return PandaEngineFactoryConstants.EXTENSIONS;
    }

    @Override
    public List<String> getMimeTypes() {
        return PandaEngineFactoryConstants.MIME_TYPES;
    }

    @Override
    public List<String> getNames() {
        return PandaEngineFactoryConstants.NAMES;
    }

    @Override
    public String getLanguageName() {
        return PandaEngineFactoryConstants.NAME;
    }

    @Override
    public String getLanguageVersion() {
        return PandaEngineFactoryConstants.VERSION;
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return pandaEngine;
    }

}
