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

package org.panda_lang.framework.language.architecture.statement;

import org.panda_lang.framework.design.architecture.dynamic.Block;
import org.panda_lang.framework.design.architecture.dynamic.ControlledBlock;
import org.panda_lang.framework.design.architecture.dynamic.Controller;
import org.panda_lang.framework.design.architecture.dynamic.Executable;
import org.panda_lang.framework.design.architecture.dynamic.Scope;
import org.panda_lang.framework.design.architecture.statement.Cell;
import org.panda_lang.framework.design.architecture.statement.Statement;

public class PandaCell implements Cell {

    private Statement statement;
    private boolean manipulated;
    private boolean executable;
    private boolean scope;
    private boolean block;
    private boolean controlledBlock;
    private boolean controller;

    public PandaCell(Statement statement) {
        this.statement = statement;
        this.update();
    }

    private void update() {
        this.executable = statement instanceof Executable;
        this.scope = statement instanceof Scope;
        this.block = statement instanceof Block;
        this.controlledBlock = statement instanceof ControlledBlock;
        this.controller = statement instanceof Controller;
    }

    @Override
    public void setStatement(Statement statement) {
        if (this.statement != null) {
            this.manipulated = true;
        }

        this.statement = statement;
        this.update();
    }

    public boolean isManipulated() {
        return manipulated;
    }

    @Override
    public boolean isExecutable() {
        return executable;
    }

    @Override
    public boolean isBlock() {
        return block;
    }

    @Override
    public boolean isController() {
        return controller;
    }

    @Override
    public boolean isScope() {
        return scope;
    }

    @Override
    public boolean isControlledBlock() {
        return controlledBlock;
    }

    @Override
    public Statement getStatement() {
        return statement;
    }

}
