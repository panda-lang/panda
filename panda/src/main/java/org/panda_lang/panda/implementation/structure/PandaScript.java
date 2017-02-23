/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.implementation.structure;

import org.panda_lang.framework.structure.Script;
import org.panda_lang.framework.structure.Statement;

import java.util.ArrayList;
import java.util.List;

public class PandaScript implements Script {

    private final String scriptName;
    private final List<Statement> statements;

    public PandaScript(String scriptName) {
        this.scriptName = scriptName;
        this.statements = new ArrayList<>();
    }

    @Override
    public List<Statement> select(Class<? extends Statement> statementClass) {
        List<Statement> selectedStatements = new ArrayList<>();

        for (Statement statement : statements) {
            if (!statementClass.isInstance(statement)) {
                continue;
            }

            selectedStatements.add(statement);
        }

        return statements;
    }

    public void addStatement(Statement statement) {
        this.statements.add(statement);
    }

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public String getScriptName() {
        return scriptName;
    }

}
