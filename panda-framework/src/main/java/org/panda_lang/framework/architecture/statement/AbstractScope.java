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

package org.panda_lang.framework.architecture.statement;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.architecture.dynamic.Executable;
import org.panda_lang.framework.interpreter.source.Localizable;
import panda.utilities.collection.Lists;
import panda.std.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractScope extends AbstractStatement implements Scope {

    protected final Scope parentScope;
    protected final FramedScope framedScope;
    protected final List<Variable> variables = new ArrayList<>();
    protected final List<Statement> statements = new ArrayList<>();

    protected AbstractScope(FramedScope framedScope, @Nullable Scope parentScope, Localizable localizable) {
        super(localizable);
        this.framedScope = framedScope;
        this.parentScope = parentScope;
    }

    protected AbstractScope(Scope parentScope, Localizable localizable) {
        this(parentScope.getFramedScope(), parentScope, localizable);
    }

    @Override
    public Statement addStatement(Statement executable) {
        return Lists.add(statements, executable);
    }

    @Override
    public boolean hasEffective(Class<? extends Statement> statementClass) {
        List<? extends Executable> executables = getExecutables();
        Executable executable = Lists.get(executables, executables.size() - 1);

        if (executable == null) {
            return false;
        }

        if (statementClass.isAssignableFrom(executable.getClass())) {
            return true;
        }

        if (executable instanceof Scope) {
            return ((Scope) executable).hasEffective(statementClass);
        }

        return false;
    }

    @Override
    public Variable createVariable(VariableData variableData) {
        return Lists.add(variables, new PandaVariable(getFramedScope().allocate(), variableData));
    }

    @Override
    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    @Override
    public boolean removeVariable(String name) {
        return getVariable(name)
                .map(variables::remove)
                .orElseGet(false);
    }

    @Override
    public Option<Variable> getVariable(String name) {
        for (Variable variable : variables) {
            if (variable.getName().equals(name)) {
                return Option.of(variable);
            }
        }

        return getParentScope().flatMap(scope -> scope.getVariable(name));
    }

    @Override
    public List<? extends Variable> getVariables() {
        return variables;
    }

    private List<? extends Executable> getExecutables() {
        return statements.stream()
                .filter(statement -> statement instanceof Executable)
                .map(statement -> (Executable) statement)
                .collect(Collectors.toList());
    }

    @Override
    public List<? extends Statement> getStatements() {
        return statements;
    }

    @Override
    public Option<Scope> getParentScope() {
        return Option.of(parentScope);
    }

    @Override
    public FramedScope getFramedScope() {
        return framedScope;
    }

}
