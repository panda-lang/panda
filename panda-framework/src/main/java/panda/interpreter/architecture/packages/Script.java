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

package panda.interpreter.architecture.packages;

import panda.interpreter.architecture.module.Module;
import panda.interpreter.architecture.statement.Statement;

import java.util.List;

/**
 *
 */
public interface Script {

    /**
     * Select all statements assignable from the specified class
     *
     * @return the selected statements
     */
    <T extends Statement> List<T> select(Class<? extends T> statementClass);

    /**
     * Add statement to the script
     *
     * @param statement the statement to add
     */
    void addStatement(Statement statement);

    /**
     * Get associated module
     *
     * @return the module
     */
    Module getModule();

    /**
     * Get script name
     *
     * @return the script name, e.g. name of file or generated name
     */
    String getName();

}
