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

package org.panda_lang.panda.language.resource.expression.subparsers.assignation;

import org.panda_lang.panda.framework.design.architecture.dynamic.ExecutableStatement;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.statement.Variable;
import org.panda_lang.panda.framework.design.runtime.ProcessStack;
import org.panda_lang.panda.language.architecture.dynamic.AbstractExecutableStatement;
import org.panda_lang.panda.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.panda.language.runtime.expression.DynamicExpression;

final class Assignation extends AbstractExecutableStatement implements DynamicExpression {

    private final ExecutableStatement assigner;
    private final Accessor<? extends Variable> accessor;

    public Assignation(Assigner<? extends Variable> assigner) {
        this.assigner = assigner.toExecutableStatement();
        this.accessor = assigner.getAccessor();
    }

    @Override
    public Object execute(ProcessStack stack, Object instance) {
        return assigner.execute(stack, instance);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) {
        return accessor.getValue(stack, instance);
    }

    @Override
    public ClassPrototype getReturnType() {
        return accessor.getTypeReference().fetch();
    }

}
