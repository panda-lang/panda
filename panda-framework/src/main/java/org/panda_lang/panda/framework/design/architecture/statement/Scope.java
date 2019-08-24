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

package org.panda_lang.panda.framework.design.architecture.statement;

import java.util.List;
import java.util.Optional;

public interface Scope extends Statement {

    /**
     * Reserve empty cell in the scope
     *
     * @return an empty cell
     */
    Cell reserveCell();

    /**
     * Add given statement to the current scope
     *
     * @param statement the statement to add
     * @return a cell where the statement was placed
     */
    Cell addStatement(Statement statement);

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
     * Get variable with the given name
     *
     * @param name the name to search for
     * @return the variable
     */
    Optional<Variable> getVariable(String name);

    /**
     * Get variables
     *
     * @return the list of variables
     */
    List<? extends Variable> getVariables();

    /**
     * Get all statements wrapped into cells
     *
     * @return list of all cells
     */
    List<? extends Cell> getCells();

    /**
     * Get parent scope
     *
     * @return the parent scope
     */
    Optional<Scope> getParent();

    /**
     * Get parent frame
     *
     * @return the parent frame
     */
    Frame getFrame();

}
