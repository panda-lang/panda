/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.architecture.statement;

import panda.std.Option;

import java.util.List;

/**
 * Represents statement that contains child statements
 */
public interface Scope extends Statement {

    /**
     * Add given statement to the current scope
     *
     * @param statement the statement to add
     * @return the statement
     */
    <S extends Statement> S addStatement(S statement);

    /**
     * Check if scope has effective (statement that is always reachable) statement of the given type
     *
     * @param statementClass type of statements to search for
     * @return true if scope has effective statement of the given type, otherwise false
     */
    boolean hasEffective(Class<? extends Statement> statementClass);

    /**
     * Create variable with given properties in the current scope
     *
     * @param variableData the data about variable
     * @return created variable
     */
    Variable createVariable(VariableData variableData);

    /**
     * Add variable to the scope
     *
     * @param variable the variable to add
     */
    void addVariable(Variable variable);

    /**
     * Remove variable with the given name
     *
     * @param name the name of variable to search for
     * @return true if variable was found and removed, otherwise false
     */
    boolean removeVariable(String name);

    /**
     * Get variable with the given name
     *
     * @param name the name to search for
     * @return the variable
     */
    Option<Variable> getVariable(String name);

    /**
     * Get variables
     *
     * @return the list of variables
     */
    List<? extends Variable> getVariables();

    /**
     * Get all statements
     *
     * @return list of all statements
     */
    List<? extends Statement> getStatements();

    /**
     * Get parent scope
     *
     * @return the parent scope
     */
    Option<Scope> getParentScope();

    default Option<StandardizedFramedScope> getStandardizedFramedScope() {
        return Option.when(getFramedScope() instanceof StandardizedFramedScope, (StandardizedFramedScope) getFramedScope());
    }

    /**
     * Get parent frame
     *
     * @return the parent frame
     */
    FramedScope getFramedScope();

}
