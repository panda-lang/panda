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

package org.panda_lang.panda.framework.design.architecture;

import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.language.architecture.AbstractScript;

public class PandaScript extends AbstractScript {

    public PandaScript(String scriptName) {
        super(scriptName);
    }

    @Override
    public String toString() {
        StringBuilder node = new StringBuilder();

        node.append("'");
        node.append(getScriptName());
        node.append("': {");

        for (Statement statement : statements) {
            node.append(System.lineSeparator());
            node.append("  ");
            node.append(statement);
            node.append(",");
        }

        node.append(System.lineSeparator());
        node.append("}");

        return node.toString();
    }

}
