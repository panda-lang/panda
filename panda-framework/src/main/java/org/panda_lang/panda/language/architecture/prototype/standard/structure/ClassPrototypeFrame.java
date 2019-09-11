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

package org.panda_lang.panda.language.architecture.prototype.standard.structure;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.statement.Frame;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.statement.Cell;
import org.panda_lang.panda.framework.design.architecture.statement.Variable;
import org.panda_lang.panda.framework.design.architecture.statement.VariableData;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.architecture.prototype.standard.PandaClassPrototype;
import org.panda_lang.panda.language.architecture.statement.AbstractStatement;

import java.util.List;
import java.util.Optional;

public class ClassPrototypeFrame extends AbstractStatement implements Frame {

    private final ClassPrototype prototype;

    public ClassPrototypeFrame(ClassPrototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public ClassPrototypeLivingFrame revive(Flow flow) {
        if (prototype instanceof PandaClassPrototype) {
            ((PandaClassPrototype) prototype).initialize();
        }

        ClassPrototypeLivingFrame instance = new ClassPrototypeLivingFrame(this, prototype);

        for (PrototypeField field : prototype.getFields().getProperties()) {
            if (!field.hasDefaultValue() || field.isStatic()) {
                continue;
            }

            Expression expression = field.getDefaultValue();
            instance.set(field.getFieldIndex(), expression.evaluate(flow));
        }

        return instance;
    }

    @Override
    public int allocate() {
        return -1;
    }

    @Override
    public Cell reserveCell() {
        return addStatement(null);
    }

    @Override
    public Cell addStatement(Statement statement) {
        throw new RuntimeException("Cannot add element to the class scope");
    }

    @Override
    public Variable createVariable(VariableData variableData) {
        throw new RuntimeException("Cannot create variable in the class scope");
    }

    @Override
    public void addVariable(Variable variable) {
        throw new RuntimeException("Cannot add variable to the class scope");
    }

    @Override
    public Optional<Variable> getVariable(String name) {
        return Optional.empty();
    }

    @Override
    public @Nullable List<Variable> getVariables() {
        return null;
    }

    @Override
    public int getRequiredMemorySize() {
        return prototype.getFields().size();
    }

    @Override
    public @Nullable List<Cell> getCells() {
        return null;
    }

    @Override
    public Optional<Scope> getParent() {
        return Optional.empty();
    }

    @Override
    public Frame getFrame() {
        return this;
    }

    public ClassPrototype getPrototype() {
        return prototype;
    }

}
