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

package org.panda_lang.panda.framework.language.architecture;

import org.panda_lang.panda.framework.design.architecture.Script;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScript implements Script {

    protected final String scriptName;
    protected final List<Statement> statements;

    public AbstractScript(String scriptName) {
        this.scriptName = scriptName;
        this.statements = new ArrayList<>();
    }

    @Override
    @SuppressWarnings({ "unchecked " })
    public <T extends Statement> List<T> select(Class<? extends T> statementClass) {
        List<T> selectedStatements = new ArrayList<>();

        for (Statement statement : statements) {
            if (!statementClass.isInstance(statement)) {
                continue;
            }

            T element = (T) statement;
            selectedStatements.add(element);
        }

        return selectedStatements;
    }

    public void addStatement(Statement statement) {
        this.statements.add(statement);
    }

    @Override
    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public String getScriptName() {
        return scriptName;
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
