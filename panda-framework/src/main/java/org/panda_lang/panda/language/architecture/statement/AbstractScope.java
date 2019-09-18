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

package org.panda_lang.panda.language.architecture.statement;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Frame;
import org.panda_lang.panda.framework.design.architecture.dynamic.Scope;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.statement.Cell;
import org.panda_lang.panda.framework.design.architecture.statement.Variable;
import org.panda_lang.panda.framework.design.architecture.statement.VariableData;
import org.panda_lang.panda.utilities.commons.collection.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractScope extends AbstractStatement implements Scope {

    protected final Frame frame;
    protected final Scope parent;
    protected final List<Variable> variables = new ArrayList<>();
    protected final List<Cell> cells = new ArrayList<>();

    protected AbstractScope(Frame frame, @Nullable Scope parent) {
        this.frame = frame;
        this.parent = parent;
    }

    protected AbstractScope(Scope parent) {
        this(parent.getFrame(), parent);
    }

    @Override
    public Cell reserveCell() {
        return addStatement(null);
    }

    @Override
    public Cell addStatement(Statement executable) {
        return Lists.add(cells, new PandaCell(executable));
    }

    @Override
    public Variable createVariable(VariableData variableData) {
        return Lists.add(variables, new PandaVariable(getFrame().allocate(), variableData));
    }

    @Override
    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    @Override
    public boolean removeVariable(String name) {
        return getVariable(name)
                .map(variables::remove)
                .orElse(false);
    }

    @Override
    public Optional<Variable> getVariable(String name) {
        for (Variable variable : variables) {
            if (variable.getName().equals(name)) {
                return Optional.of(variable);
            }
        }

        return getParent().flatMap(scope -> scope.getVariable(name));
    }

    @Override
    public List<? extends Variable> getVariables() {
        return variables;
    }

    @Override
    public List<? extends Cell> getCells() {
        return cells;
    }

    @Override
    public Optional<Scope> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

}
