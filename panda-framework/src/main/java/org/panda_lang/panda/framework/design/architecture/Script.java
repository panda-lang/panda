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

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;

import java.util.List;

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
     * Get script content
     *
     * @return list of the statement declarations
     */
    List<? extends Statement> getStatements();

    /**
     * Get module loader used by script
     *
     * @return the module loader
     */
    ModuleLoader getModuleLoader();

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
    String getScriptName();

}
