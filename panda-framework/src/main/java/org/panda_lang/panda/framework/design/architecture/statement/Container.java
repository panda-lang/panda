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

package org.panda_lang.panda.framework.design.architecture.statement;

import java.util.List;

public interface Container extends Statement {

    /**
     * Reserve empty cell in the container
     *
     * @return an empty cell
     */
    StatementCell reserveCell();

    /**
     * Adds executable to the current scope
     *
     * @param statement proper statement
     * @return executable cell where executable was placed
     */
    StatementCell addStatement(Statement statement);

    /**
     * @return list of all cells in correct order
     */
    List<? extends StatementCell> getStatementCells();

}
