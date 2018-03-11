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

package org.panda_lang.panda.language.structure.prototype.scope;

import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.architecture.detach.Scope;
import org.panda_lang.panda.framework.design.architecture.detach.StatementCell;
import org.panda_lang.panda.framework.design.architecture.Statement;
import org.panda_lang.panda.framework.design.architecture.detach.ExecutableBranch;
import org.panda_lang.panda.framework.design.architecture.detach.Expression;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;

import java.util.List;

public class ClassScope implements Scope {

    private final ClassPrototype prototype;

    public ClassScope(ClassPrototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public ClassScopeInstance createInstance(ExecutableBranch branch) {
        if (prototype instanceof PandaClassPrototype) {
            ((PandaClassPrototype) prototype).initialize();
        }

        ClassScopeInstance instance = new ClassScopeInstance(this, prototype);

        for (PrototypeField field : prototype.getFields()) {
            if (!field.hasDefaultValue() || field.isStatic()) {
                continue;
            }

            Expression expression = field.getDefaultValue();
            instance.getFieldValues()[field.getFieldIndex()] = expression.getExpressionValue(branch);
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
    public List<Variable> getVariables() {
        return null;
    }

    @Override
    public List<StatementCell> getStatementCells() {
        return null;
    }

}
