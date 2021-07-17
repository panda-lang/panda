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

package panda.interpreter.architecture.dynamic.assigner;

import panda.interpreter.architecture.dynamic.Executable;
import panda.interpreter.architecture.expression.Expressible;
import panda.interpreter.architecture.statement.Variable;
import panda.interpreter.architecture.dynamic.accessor.Accessor;

public interface Assigner<T extends Variable> extends Executable, Expressible {

    Accessor<T> getAccessor();

    @Override
    default AssignerExpression toExpression() {
        return new AssignerExpression(this);
    }

}
