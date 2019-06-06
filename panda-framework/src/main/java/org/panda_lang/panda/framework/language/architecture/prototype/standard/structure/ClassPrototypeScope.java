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

package org.panda_lang.panda.framework.language.architecture.prototype.standard.structure;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.PandaClassPrototype;
import org.panda_lang.panda.framework.language.architecture.statement.AbstractStatement;

import java.util.List;

public class ClassPrototypeScope extends AbstractStatement implements Scope {

    private final ClassPrototype prototype;

    public ClassPrototypeScope(ClassPrototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public ClassPrototypeScopeFrame createInstance(ExecutableBranch branch) {
        if (prototype instanceof PandaClassPrototype) {
            ((PandaClassPrototype) prototype).initialize();
        }

        ClassPrototypeScopeFrame instance = new ClassPrototypeScopeFrame(this, prototype);

        for (PrototypeField field : prototype.getFields().getListOfFields()) {
            if (!field.hasDefaultValue() || field.isStatic()) {
                continue;
            }

            Expression expression = field.getDefaultValue();
            instance.set(field.getFieldIndex(), expression.getExpressionValue(branch));
        }

        return instance;
    }

    @Override
    public StatementCell reserveCell() {
        return addStatement(null);
    }

    @Override
    public StatementCell addStatement(Statement statement) {
        throw new RuntimeException("Cannot add element to the class scope");
    }

    @Override
    public @Nullable List<Variable> getVariables() {
        return null;
    }

    @Override
    public @Nullable List<StatementCell> getStatementCells() {
        return null;
    }

    public ClassPrototype getPrototype() {
        return prototype;
    }

}
